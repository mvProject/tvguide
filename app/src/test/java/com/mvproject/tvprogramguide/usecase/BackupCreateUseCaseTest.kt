package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.BackupCreateUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class BackupCreateUseCaseTest : FunSpec({

    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var selectedChannelRepository: ISelectedChannelRepository
    lateinit var channelListRepository: IChannelListRepository
    lateinit var useCase: BackupCreateUseCase

    beforeTest {
        preferenceRepository = mockk()
        selectedChannelRepository = mockk()
        channelListRepository = mockk()
        useCase = BackupCreateUseCase(
            preferenceRepository = preferenceRepository,
            selectedChannelRepository = selectedChannelRepository,
            channelListRepository = channelListRepository,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("backup includes app settings theme and view count from preferences") {
            val settings = AppSettingsModel(
                appTheme = AppThemeOptions.DARK.id,
                programsViewCount = 5,
                channelsUpdatePeriod = 14,
                programsUpdatePeriod = 4,
            )
            val playlist = ChannelList(id = 1, listName = "Sports", isSelected = true)
            val channels = listOf(
                SelectionChannel(channelId = "ch1", order = 1),
            )

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(playlist)
            coEvery { selectedChannelRepository.loadSelectedChannels("Sports") } returns channels

            val result = useCase(listOf("Sports"))

            result.theme shouldBe AppThemeOptions.DARK.id
            result.programsViewCount shouldBe 5
            result.channelsUpdatePeriod shouldBe 14
            result.programsUpdatePeriod shouldBe 4

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("Sports") }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }

        test("channels in playlist backup are sorted by order field") {
            val settings = AppSettingsModel()
            val playlist = ChannelList(id = 1, listName = "Movies", isSelected = false)
            // Deliberately out of order to verify sorting
            val channels = listOf(
                SelectionChannel(channelId = "ch3", order = 3),
                SelectionChannel(channelId = "ch1", order = 1),
                SelectionChannel(channelId = "ch2", order = 2),
            )

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(playlist)
            coEvery { selectedChannelRepository.loadSelectedChannels("Movies") } returns channels

            val result = useCase(listOf("Movies"))

            result.channelsData.size shouldBe 1
            result.channelsData[0].content shouldBe listOf("ch1", "ch2", "ch3")

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("Movies") }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }

        test("playlist backup preserves id, name, and isSelected from the channel list") {
            val settings = AppSettingsModel()
            val playlist = ChannelList(id = 42, listName = "News", isSelected = true)
            val channels = listOf(SelectionChannel(channelId = "ch1", order = 1))

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(playlist)
            coEvery { selectedChannelRepository.loadSelectedChannels("News") } returns channels

            val result = useCase(listOf("News"))

            result.channelsData.size shouldBe 1
            val playlistBackup = result.channelsData[0]
            playlistBackup.id shouldBe 42
            playlistBackup.name shouldBe "News"
            playlistBackup.isSelected shouldBe true

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("News") }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }

        test("list name not found in playlists is skipped gracefully") {
            val settings = AppSettingsModel()
            val playlist = ChannelList(id = 1, listName = "Sports", isSelected = false)

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(playlist)

            // "Unknown" has no matching playlist, "Sports" does
            val channels = listOf(SelectionChannel(channelId = "ch1", order = 1))
            coEvery { selectedChannelRepository.loadSelectedChannels("Sports") } returns channels

            val result = useCase(listOf("Unknown", "Sports"))

            // Only "Sports" should appear in the backup; "Unknown" is skipped
            result.channelsData.size shouldBe 1
            result.channelsData[0].name shouldBe "Sports"

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("Sports") }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }

        test("empty lists argument produces backup with no channelsData") {
            val settings = AppSettingsModel()

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns emptyList()

            val result = useCase(emptyList())

            result.channelsData shouldBe emptyList()

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }

        test("multiple playlists are all included in the backup in request order") {
            val settings = AppSettingsModel()
            val playlistA = ChannelList(id = 1, listName = "Sports", isSelected = true)
            val playlistB = ChannelList(id = 2, listName = "Movies", isSelected = false)
            val channelsA = listOf(SelectionChannel(channelId = "ch1", order = 1))
            val channelsB = listOf(
                SelectionChannel(channelId = "ch3", order = 2),
                SelectionChannel(channelId = "ch2", order = 1),
            )

            every { preferenceRepository.loadAppSettings() } returns flowOf(settings)
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                playlistA,
                playlistB
            )
            coEvery { selectedChannelRepository.loadSelectedChannels("Sports") } returns channelsA
            coEvery { selectedChannelRepository.loadSelectedChannels("Movies") } returns channelsB

            val result = useCase(listOf("Sports", "Movies"))

            result.channelsData.size shouldBe 2
            result.channelsData[0].name shouldBe "Sports"
            result.channelsData[0].content shouldBe listOf("ch1")
            result.channelsData[1].name shouldBe "Movies"
            result.channelsData[1].content shouldBe listOf("ch2", "ch3")

            verify(exactly = 1) { preferenceRepository.loadAppSettings() }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("Sports") }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels("Movies") }
            confirmVerified(preferenceRepository, selectedChannelRepository, channelListRepository)
        }
    }
})
