package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.BackupRestoreUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll

class BackupRestoreUseCaseTest : FunSpec({

    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var channelListRepository: IChannelListRepository
    lateinit var allChannelRepository: IAllChannelRepository
    lateinit var selectedChannelRepository: ISelectedChannelRepository
    lateinit var useCase: BackupRestoreUseCase

    beforeTest {
        preferenceRepository = mockk()
        channelListRepository = mockk()
        allChannelRepository = mockk()
        selectedChannelRepository = mockk()
        useCase = BackupRestoreUseCase(
            preferenceRepository = preferenceRepository,
            channelListRepository = channelListRepository,
            allChannelRepository = allChannelRepository,
            selectedChannelRepository = selectedChannelRepository,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("app settings from backup are written to preference repository") {
            val capturedSettings = slot<AppSettingsModel>()
            val backup = TvBackup(
                theme = AppThemeOptions.DARK.id,
                programsViewCount = 6,
                channelsUpdatePeriod = 14,
                programsUpdatePeriod = 4,
                channelsData = emptyList(),
            )

            coEvery { preferenceRepository.setAppSettings(capture(capturedSettings)) } just Runs
            coEvery { channelListRepository.loadChannelsLists() } returns emptyList()

            useCase(backup)

            capturedSettings.captured.appTheme shouldBe AppThemeOptions.DARK.id
            capturedSettings.captured.programsViewCount shouldBe 6
            capturedSettings.captured.channelsUpdatePeriod shouldBe 14
            capturedSettings.captured.programsUpdatePeriod shouldBe 4

            coVerify(exactly = 1) { preferenceRepository.setAppSettings(any()) }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            confirmVerified(
                preferenceRepository,
                channelListRepository,
                allChannelRepository,
                selectedChannelRepository
            )
        }

        test("when no channel list has isSelected=true, the first restored list gets isSelected=true") {
            val backup = TvBackup(
                channelsData = listOf(
                    TvBackup.PlaylistBackup(
                        id = 1,
                        name = "Sports",
                        isSelected = false,
                        content = emptyList()
                    ),
                    TvBackup.PlaylistBackup(
                        id = 2,
                        name = "Movies",
                        isSelected = false,
                        content = emptyList()
                    ),
                )
            )
            val restoredSports = ChannelList(id = 1, listName = "Sports", isSelected = false)
            val restoredMovies = ChannelList(id = 2, listName = "Movies", isSelected = false)
            val capturedCalls = mutableListOf<ChannelList>()

            coEvery { preferenceRepository.setAppSettings(any()) } just Runs
            coEvery { channelListRepository.addChannelList(capture(capturedCalls)) } just Runs
            coEvery { allChannelRepository.loadChannelsById(emptyList()) } returns emptyList()
            coEvery { selectedChannelRepository.addChannels(any(), any()) } just Runs
            // After restoring both lists, neither is selected
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                restoredSports,
                restoredMovies
            )

            useCase(backup)

            // The first list (Sports) should be promoted to isSelected=true
            capturedCalls.last().listName shouldBe "Sports"
            capturedCalls.last().isSelected shouldBe true

            coVerify(exactly = 1) { preferenceRepository.setAppSettings(any()) }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            // addChannelList is called once per playlist + once for the default promotion
            coVerify(exactly = 3) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 2) { allChannelRepository.loadChannelsById(emptyList()) }
            coVerify(exactly = 2) { selectedChannelRepository.addChannels(any(), emptyList()) }
            confirmVerified(
                preferenceRepository,
                channelListRepository,
                allChannelRepository,
                selectedChannelRepository
            )
        }

        test("when a channel list already has isSelected=true, no default promotion occurs") {
            val backup = TvBackup(
                channelsData = listOf(
                    TvBackup.PlaylistBackup(
                        id = 1,
                        name = "Sports",
                        isSelected = true,
                        content = emptyList()
                    ),
                    TvBackup.PlaylistBackup(
                        id = 2,
                        name = "Movies",
                        isSelected = false,
                        content = emptyList()
                    ),
                )
            )
            val restoredSports = ChannelList(id = 1, listName = "Sports", isSelected = true)
            val restoredMovies = ChannelList(id = 2, listName = "Movies", isSelected = false)

            coEvery { preferenceRepository.setAppSettings(any()) } just Runs
            coEvery { channelListRepository.addChannelList(any()) } just Runs
            coEvery { allChannelRepository.loadChannelsById(emptyList()) } returns emptyList()
            coEvery { selectedChannelRepository.addChannels(any(), any()) } just Runs
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                restoredSports,
                restoredMovies
            )

            useCase(backup)

            // addChannelList called exactly once per playlist, no extra call for default
            coVerify(exactly = 2) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { preferenceRepository.setAppSettings(any()) }
            coVerify(exactly = 2) { allChannelRepository.loadChannelsById(emptyList()) }
            coVerify(exactly = 2) { selectedChannelRepository.addChannels(any(), emptyList()) }
            confirmVerified(
                preferenceRepository,
                channelListRepository,
                allChannelRepository,
                selectedChannelRepository
            )
        }

        test("channels loaded from allChannelRepository are saved with parentList set to list name") {
            val channelIds = listOf("ch1", "ch2")
            val backup = TvBackup(
                channelsData = listOf(
                    TvBackup.PlaylistBackup(
                        id = 1,
                        name = "Sports",
                        isSelected = true,
                        content = channelIds
                    ),
                )
            )
            val loadedChannels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
            )
            val capturedListName = slot<String>()
            val capturedChannels = slot<List<SelectionChannel>>()

            coEvery { preferenceRepository.setAppSettings(any()) } just Runs
            coEvery { channelListRepository.addChannelList(any()) } just Runs
            coEvery { allChannelRepository.loadChannelsById(channelIds) } returns loadedChannels
            coEvery {
                selectedChannelRepository.addChannels(
                    capture(capturedListName),
                    capture(capturedChannels)
                )
            } just Runs
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                ChannelList(id = 1, listName = "Sports", isSelected = true)
            )

            useCase(backup)

            capturedListName.captured shouldBe "Sports"
            // updateOrders assigns order starting from 1
            capturedChannels.captured[0].order shouldBe 1
            capturedChannels.captured[1].order shouldBe 2
            capturedChannels.captured.all { it.parentList == "Sports" } shouldBe true

            coVerify(exactly = 1) { preferenceRepository.setAppSettings(any()) }
            coVerify(exactly = 1) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 1) { allChannelRepository.loadChannelsById(channelIds) }
            coVerify(exactly = 1) { selectedChannelRepository.addChannels("Sports", any()) }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            confirmVerified(
                preferenceRepository,
                channelListRepository,
                allChannelRepository,
                selectedChannelRepository
            )
        }

        test("backup with no channelsData only restores settings and checks lists") {
            val backup = TvBackup(channelsData = emptyList())

            coEvery { preferenceRepository.setAppSettings(any()) } just Runs
            coEvery { channelListRepository.loadChannelsLists() } returns emptyList()

            useCase(backup)

            coVerify(exactly = 1) { preferenceRepository.setAppSettings(any()) }
            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 0) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 0) { allChannelRepository.loadChannelsById(any()) }
            coVerify(exactly = 0) { selectedChannelRepository.addChannels(any(), any()) }
            confirmVerified(
                preferenceRepository,
                channelListRepository,
                allChannelRepository,
                selectedChannelRepository
            )
        }
    }
})
