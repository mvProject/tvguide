package com.mvproject.tvprogramguide.usecase

import app.cash.turbine.test
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsWithProgramsUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class GetSelectedChannelsWithProgramsUseCaseTest : FunSpec({

    lateinit var selectedChannelRepository: ISelectedChannelRepository
    lateinit var programRepository: IProgramRepository
    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var useCase: GetSelectedChannelsWithProgramsUseCase

    beforeTest {
        selectedChannelRepository = mockk()
        programRepository = mockk()
        preferenceRepository = mockk()
        useCase = GetSelectedChannelsWithProgramsUseCase(
            selectedChannelRepository = selectedChannelRepository,
            programRepository = programRepository,
            preferenceRepository = preferenceRepository,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("emits selected channels paired with their programs") {
            val channel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "Channel 1",
                channelIcon = "icon1.png",
            )
            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = 1000L,
                    dateTimeEnd = 2000L,
                    channel = "prog_ch1"
                ),
                Program(
                    programId = "p2",
                    dateTimeStart = 2000L,
                    dateTimeEnd = 3000L,
                    channel = "prog_ch1"
                ),
            )
            val settings = AppSettingsModel(programsViewCount = 0)

            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(
                    channel
                )
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { programRepository.loadProgramsForChannels(listOf("prog_ch1")) } returns programs

            useCase().test {
                val emission = awaitItem()
                emission.size shouldBe 1
                emission[0].selectedChannel shouldBe channel
                emission[0].programs.size shouldBe 2
                awaitComplete()
            }

            coVerify(exactly = 1) { programRepository.loadProgramsForChannels(listOf("prog_ch1")) }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }

        test("broken channels (blank name and icon) are filtered out before loading programs") {
            val goodChannel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "Channel 1",
                channelIcon = "icon1.png",
                parentList = "Sports",
            )
            val brokenChannel = SelectionChannel(
                channelId = "sel2",
                programId = "prog_ch2",
                channelName = "",
                channelIcon = "",
                parentList = "Sports",
            )
            val settings = AppSettingsModel(programsViewCount = 0)
            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = 1000L,
                    dateTimeEnd = 2000L,
                    channel = "prog_ch1"
                ),
            )

            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(
                    goodChannel,
                    brokenChannel
                )
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            // Only goodChannel's programId should be requested
            coEvery { programRepository.loadProgramsForChannels(listOf("prog_ch1")) } returns programs
            // After filtering, addChannels is called to clean up the broken entry
            coEvery {
                selectedChannelRepository.addChannels(
                    "Sports",
                    listOf(goodChannel)
                )
            } just Runs

            useCase().test {
                val emission = awaitItem()
                // Only the good channel appears in the result
                emission.size shouldBe 1
                emission[0].selectedChannel shouldBe goodChannel
                awaitComplete()
            }

            coVerify(exactly = 1) { programRepository.loadProgramsForChannels(listOf("prog_ch1")) }
            coVerify(exactly = 1) {
                selectedChannelRepository.addChannels(
                    "Sports",
                    listOf(goodChannel)
                )
            }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }

        test("programsViewCount limit is applied — only that many programs are returned per channel") {
            val channel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "Channel 1",
                channelIcon = "icon1.png",
            )
            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = 1000L,
                    dateTimeEnd = 2000L,
                    channel = "prog_ch1"
                ),
                Program(
                    programId = "p2",
                    dateTimeStart = 2000L,
                    dateTimeEnd = 3000L,
                    channel = "prog_ch1"
                ),
                Program(
                    programId = "p3",
                    dateTimeStart = 3000L,
                    dateTimeEnd = 4000L,
                    channel = "prog_ch1"
                ),
                Program(
                    programId = "p4",
                    dateTimeStart = 4000L,
                    dateTimeEnd = 5000L,
                    channel = "prog_ch1"
                ),
                Program(
                    programId = "p5",
                    dateTimeStart = 5000L,
                    dateTimeEnd = 6000L,
                    channel = "prog_ch1"
                ),
            )
            // Limit to 3 programs
            val settings = AppSettingsModel(programsViewCount = 3)

            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(
                    channel
                )
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { programRepository.loadProgramsForChannels(listOf("prog_ch1")) } returns programs

            useCase().test {
                val emission = awaitItem()
                emission.size shouldBe 1
                emission[0].programs.size shouldBe 3
                awaitComplete()
            }

            coVerify(exactly = 1) { programRepository.loadProgramsForChannels(listOf("prog_ch1")) }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }

        test("channel with no matching programs emits with empty program list") {
            val channel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "Channel 1",
                channelIcon = "icon1.png",
            )
            val settings = AppSettingsModel(programsViewCount = 3)

            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(
                    channel
                )
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            // Programs list has no entry for prog_ch1
            coEvery { programRepository.loadProgramsForChannels(listOf("prog_ch1")) } returns emptyList()

            useCase().test {
                val emission = awaitItem()
                emission.size shouldBe 1
                emission[0].selectedChannel shouldBe channel
                emission[0].programs shouldBe emptyList()
                awaitComplete()
            }

            coVerify(exactly = 1) { programRepository.loadProgramsForChannels(listOf("prog_ch1")) }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }

        test("when all channels are broken, result is empty and addChannels is called with empty list") {
            val brokenChannel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "",
                channelIcon = "",
                parentList = "Sports",
            )
            val settings = AppSettingsModel(programsViewCount = 0)

            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(
                    brokenChannel
                )
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { programRepository.loadProgramsForChannels(emptyList()) } returns emptyList()
            coEvery { selectedChannelRepository.addChannels("", emptyList()) } just Runs

            useCase().test {
                val emission = awaitItem()
                emission shouldBe emptyList()
                awaitComplete()
            }

            coVerify(exactly = 1) { programRepository.loadProgramsForChannels(emptyList()) }
            // parentList of actualChannels (which is empty) → firstOrNull returns null → String.empty ("")
            coVerify(exactly = 1) { selectedChannelRepository.addChannels("", emptyList()) }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }

        test("emits for each combined flow update") {
            val channel = SelectionChannel(
                channelId = "sel1",
                programId = "prog_ch1",
                channelName = "Channel 1",
                channelIcon = "icon1.png",
            )
            val settings = AppSettingsModel(programsViewCount = 0)
            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = 1000L,
                    dateTimeEnd = 2000L,
                    channel = "prog_ch1"
                ),
            )

            // Two emissions from the selected channels flow
            every { selectedChannelRepository.loadSelectedChannelsAsFlow() } returns flowOf(
                listOf(channel),
                listOf(channel),
            )
            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { programRepository.loadProgramsForChannels(listOf("prog_ch1")) } returns programs

            useCase().test {
                val first = awaitItem()
                first.size shouldBe 1
                val second = awaitItem()
                second.size shouldBe 1
                awaitComplete()
            }

            coVerify(exactly = 2) { programRepository.loadProgramsForChannels(listOf("prog_ch1")) }
            verify(exactly = 1) { selectedChannelRepository.loadSelectedChannelsAsFlow() }
            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            confirmVerified(selectedChannelRepository, programRepository, preferenceRepository)
        }
    }
})
