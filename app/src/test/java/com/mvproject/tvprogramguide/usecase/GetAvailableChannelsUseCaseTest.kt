package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.GetAvailableChannelsUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll

class GetAvailableChannelsUseCaseTest : FunSpec({

    lateinit var allChannelRepository: IAllChannelRepository
    lateinit var selectedChannelRepository: ISelectedChannelRepository
    lateinit var useCase: GetAvailableChannelsUseCase

    beforeTest {
        allChannelRepository = mockk()
        selectedChannelRepository = mockk()
        useCase = GetAvailableChannelsUseCase(allChannelRepository, selectedChannelRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("channels present in the selected list have isSelected=true, others have isSelected=false") {
            val listName = "My List"
            val allChannels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
                SelectionChannel(channelId = "ch3", channelName = "Channel 3"),
            )
            val selectedChannels = listOf(
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
            )

            coEvery { allChannelRepository.loadChannels() } returns allChannels
            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns selectedChannels

            val result = useCase(listName)

            result.size shouldBe 3

            val ch1 = result.first { it.channelId == "ch1" }
            val ch2 = result.first { it.channelId == "ch2" }
            val ch3 = result.first { it.channelId == "ch3" }

            ch1.isSelected shouldBe false
            ch2.isSelected shouldBe true
            ch3.isSelected shouldBe false

            coVerify(exactly = 1) { allChannelRepository.loadChannels() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(allChannelRepository, selectedChannelRepository)
        }

        test("all returned channels have parentList set to the listName parameter") {
            val listName = "Favorites"
            val allChannels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
            )
            val selectedChannels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
            )

            coEvery { allChannelRepository.loadChannels() } returns allChannels
            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns selectedChannels

            val result = useCase(listName)

            result.forEach { channel ->
                channel.parentList shouldBe listName
            }

            coVerify(exactly = 1) { allChannelRepository.loadChannels() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(allChannelRepository, selectedChannelRepository)
        }

        test("when no channels are selected, all channels have isSelected=false") {
            val listName = "Empty List"
            val allChannels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
            )

            coEvery { allChannelRepository.loadChannels() } returns allChannels
            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns emptyList()

            val result = useCase(listName)

            result.all { !it.isSelected } shouldBe true

            coVerify(exactly = 1) { allChannelRepository.loadChannels() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(allChannelRepository, selectedChannelRepository)
        }

        test("when all channels are selected, all channels have isSelected=true") {
            val listName = "All Selected"
            val channels = listOf(
                SelectionChannel(channelId = "ch1", channelName = "Channel 1"),
                SelectionChannel(channelId = "ch2", channelName = "Channel 2"),
            )

            coEvery { allChannelRepository.loadChannels() } returns channels
            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns channels

            val result = useCase(listName)

            result.all { it.isSelected } shouldBe true

            coVerify(exactly = 1) { allChannelRepository.loadChannels() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(allChannelRepository, selectedChannelRepository)
        }

        test("when available channels list is empty, result is empty") {
            val listName = "My List"

            coEvery { allChannelRepository.loadChannels() } returns emptyList()
            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns emptyList()

            val result = useCase(listName)

            result shouldBe emptyList()

            coVerify(exactly = 1) { allChannelRepository.loadChannels() }
            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(allChannelRepository, selectedChannelRepository)
        }
    }
})
