package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.parse.ChannelParseModel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.network.NetworkClient
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.utils.TimeUtils
import io.kotest.core.spec.style.FunSpec
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll

class UpdateChannelsInfoUseCaseTest : FunSpec({

    lateinit var allChannelRepository: IAllChannelRepository
    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var programDataSource: IProgramDataSource
    lateinit var useCase: UpdateChannelsInfoUseCase

    beforeTest {
        allChannelRepository = mockk()
        preferenceRepository = mockk()
        programDataSource = mockk()
        useCase = UpdateChannelsInfoUseCase(
            allChannelRepository = allChannelRepository,
            preferenceRepository = preferenceRepository,
            programDataSource = programDataSource,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("url passed to downloadAndParseChannels matches EPG_CHANNELS_URL constant") {
            val currentTime = 10_000_000L
            val urlSlot = slot<String>()

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime

            // datasource produces no callbacks → empty channel list
            coEvery {
                programDataSource.downloadAndParseChannels(
                    url = capture(urlSlot),
                    onChannelParsed = any(),
                )
            } just Runs
            coEvery { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) } just Runs

            useCase()

            assert(urlSlot.captured == NetworkClient.EPG_CHANNELS_URL)

            coVerify(exactly = 1) {
                programDataSource.downloadAndParseChannels(
                    url = NetworkClient.EPG_CHANNELS_URL,
                    onChannelParsed = any(),
                )
            }
            coVerify(exactly = 1) { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) }
            confirmVerified(allChannelRepository, preferenceRepository, programDataSource)
        }

        test("when datasource returns channels, updateChannels is called with the parsed channel list") {
            val currentTime = 10_000_000L
            val fakeChannels = listOf(
                ChannelParseModel(id = "ch1", name = "Channel 1", logo = "logo1.png"),
                ChannelParseModel(id = "ch2", name = "Channel 2", logo = "logo2.png"),
            )
            val expectedResponses = listOf(
                AvailableChannelResponse(
                    channelId = "ch1",
                    channelName = "Channel 1",
                    channelIcon = "logo1.png"
                ),
                AvailableChannelResponse(
                    channelId = "ch2",
                    channelName = "Channel 2",
                    channelIcon = "logo2.png"
                ),
            )

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime

            coEvery {
                programDataSource.downloadAndParseChannels(url = any(), onChannelParsed = any())
            } coAnswers {
                val callback = arg<suspend (ChannelParseModel) -> Unit>(1)
                fakeChannels.forEach { callback(it) }
            }

            coEvery { allChannelRepository.updateChannels(channels = expectedResponses) } just Runs
            coEvery { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) } just Runs

            useCase()

            coVerify(exactly = 1) {
                programDataSource.downloadAndParseChannels(
                    url = NetworkClient.EPG_CHANNELS_URL,
                    onChannelParsed = any(),
                )
            }
            coVerify(exactly = 1) { allChannelRepository.updateChannels(channels = expectedResponses) }
            coVerify(exactly = 1) { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) }
            confirmVerified(allChannelRepository, preferenceRepository, programDataSource)
        }

        test("when datasource returns empty channels, updateChannels is NOT called") {
            val currentTime = 10_000_000L

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime

            // datasource invokes callback zero times → networkChannels list stays empty
            coEvery {
                programDataSource.downloadAndParseChannels(url = any(), onChannelParsed = any())
            } just Runs

            coEvery { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) } just Runs

            useCase()

            coVerify(exactly = 1) {
                programDataSource.downloadAndParseChannels(
                    url = NetworkClient.EPG_CHANNELS_URL,
                    onChannelParsed = any(),
                )
            }
            coVerify(exactly = 0) { allChannelRepository.updateChannels(any()) }
            coVerify(exactly = 1) { preferenceRepository.setChannelsUpdateLastTime(timeInMillis = currentTime) }
            confirmVerified(allChannelRepository, preferenceRepository, programDataSource)
        }
    }
})
