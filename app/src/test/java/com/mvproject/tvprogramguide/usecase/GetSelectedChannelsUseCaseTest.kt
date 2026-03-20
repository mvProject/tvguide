package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll

class GetSelectedChannelsUseCaseTest : FunSpec({

    lateinit var selectedChannelRepository: ISelectedChannelRepository
    lateinit var useCase: GetSelectedChannelsUseCase

    beforeTest {
        selectedChannelRepository = mockk()
        useCase = GetSelectedChannelsUseCase(selectedChannelRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("returns channels sorted ascending by order field") {
            val listName = "My List"
            // Repository returns channels in a scrambled order
            val unsortedChannels = listOf(
                SelectionChannel(channelId = "ch3", order = 3),
                SelectionChannel(channelId = "ch1", order = 1),
                SelectionChannel(channelId = "ch2", order = 2),
            )

            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns unsortedChannels

            val result = useCase(listName)

            result.map { it.channelId } shouldBe listOf("ch1", "ch2", "ch3")
            result.map { it.order } shouldBe listOf(1, 2, 3)

            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(selectedChannelRepository)
        }

        test("when channels are already sorted by order, result order is preserved") {
            val listName = "Sorted List"
            val sortedChannels = listOf(
                SelectionChannel(channelId = "ch1", order = 10),
                SelectionChannel(channelId = "ch2", order = 20),
                SelectionChannel(channelId = "ch3", order = 30),
            )

            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns sortedChannels

            val result = useCase(listName)

            result.map { it.channelId } shouldBe listOf("ch1", "ch2", "ch3")

            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(selectedChannelRepository)
        }

        test("when repository returns empty list, result is empty") {
            val listName = "Empty"

            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns emptyList()

            val result = useCase(listName)

            result shouldBe emptyList()

            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(selectedChannelRepository)
        }

        test("when repository returns a single channel, result contains that channel") {
            val listName = "Single"
            val singleChannel = listOf(SelectionChannel(channelId = "ch1", order = 5))

            coEvery { selectedChannelRepository.loadSelectedChannels(listName = listName) } returns singleChannel

            val result = useCase(listName)

            result.size shouldBe 1
            result.first().channelId shouldBe "ch1"

            coVerify(exactly = 1) { selectedChannelRepository.loadSelectedChannels(listName = listName) }
            confirmVerified(selectedChannelRepository)
        }
    }
})
