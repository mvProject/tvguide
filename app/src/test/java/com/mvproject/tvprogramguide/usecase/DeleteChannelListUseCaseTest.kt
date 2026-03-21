package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
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

class DeleteChannelListUseCaseTest : FunSpec({

    lateinit var channelListRepository: IChannelListRepository
    lateinit var useCase: DeleteChannelListUseCase

    beforeTest {
        channelListRepository = mockk()
        useCase = DeleteChannelListUseCase(channelListRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("when deleting a selected list, the first non-selected list gets isSelected=true") {
            val listToDelete = ChannelList(id = 1, listName = "Selected List", isSelected = true)
            val otherList = ChannelList(id = 2, listName = "Other List", isSelected = false)
            val capturedUpdate = slot<ChannelList>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                listToDelete,
                otherList
            )
            coEvery { channelListRepository.addChannelList(capture(capturedUpdate)) } just Runs
            coEvery { channelListRepository.deleteList(listToDelete) } just Runs

            useCase(listToDelete)

            capturedUpdate.captured.id shouldBe otherList.id
            capturedUpdate.captured.listName shouldBe otherList.listName
            capturedUpdate.captured.isSelected shouldBe true

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelList(otherList.copy(isSelected = true)) }
            coVerify(exactly = 1) { channelListRepository.deleteList(listToDelete) }
            confirmVerified(channelListRepository)
        }

        test("when deleting a selected list with no other lists, nothing is reassigned and list is deleted") {
            val listToDelete = ChannelList(id = 1, listName = "Only List", isSelected = true)

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(listToDelete)
            coEvery { channelListRepository.deleteList(listToDelete) } just Runs

            useCase(listToDelete)

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 0) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 1) { channelListRepository.deleteList(listToDelete) }
            confirmVerified(channelListRepository)
        }

        test("when deleting a non-selected list, no reassignment occurs and list is deleted") {
            val listToDelete =
                ChannelList(id = 2, listName = "Non-Selected List", isSelected = false)

            coEvery { channelListRepository.deleteList(listToDelete) } just Runs

            useCase(listToDelete)

            coVerify(exactly = 0) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 0) { channelListRepository.addChannelList(any()) }
            coVerify(exactly = 1) { channelListRepository.deleteList(listToDelete) }
            confirmVerified(channelListRepository)
        }

        test("when deleting a selected list, the first non-selected list among many is reassigned") {
            val listToDelete = ChannelList(id = 1, listName = "Selected List", isSelected = true)
            val secondList = ChannelList(id = 2, listName = "Second List", isSelected = false)
            val thirdList = ChannelList(id = 3, listName = "Third List", isSelected = false)
            val capturedUpdate = slot<ChannelList>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                listToDelete,
                secondList,
                thirdList
            )
            coEvery { channelListRepository.addChannelList(capture(capturedUpdate)) } just Runs
            coEvery { channelListRepository.deleteList(listToDelete) } just Runs

            useCase(listToDelete)

            // firstOrNull { !it.isSelected } picks secondList (id=2)
            capturedUpdate.captured.id shouldBe secondList.id
            capturedUpdate.captured.isSelected shouldBe true

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelList(secondList.copy(isSelected = true)) }
            coVerify(exactly = 1) { channelListRepository.deleteList(listToDelete) }
            confirmVerified(channelListRepository)
        }
    }
})
