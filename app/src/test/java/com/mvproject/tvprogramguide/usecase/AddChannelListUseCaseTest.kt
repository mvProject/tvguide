package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
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

class AddChannelListUseCaseTest : FunSpec({

    lateinit var channelListRepository: IChannelListRepository
    lateinit var useCase: AddChannelListUseCase

    beforeTest {
        channelListRepository = mockk()
        useCase = AddChannelListUseCase(channelListRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("when repository has no existing lists, new list is added with isSelected=true") {
            val capturedList = slot<ChannelList>()
            coEvery { channelListRepository.loadChannelsLists() } returns emptyList()
            coEvery { channelListRepository.addChannelList(capture(capturedList)) } just Runs

            useCase("My First List")

            capturedList.captured.listName shouldBe "My First List"
            capturedList.captured.isSelected shouldBe true

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelList(any()) }
            confirmVerified(channelListRepository)
        }

        test("when repository already has existing lists, new list is added with isSelected=false") {
            val existingList = ChannelList(id = 1, listName = "Existing", isSelected = true)
            val capturedList = slot<ChannelList>()
            coEvery { channelListRepository.loadChannelsLists() } returns listOf(existingList)
            coEvery { channelListRepository.addChannelList(capture(capturedList)) } just Runs

            useCase("My Second List")

            capturedList.captured.listName shouldBe "My Second List"
            capturedList.captured.isSelected shouldBe false

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelList(any()) }
            confirmVerified(channelListRepository)
        }

        test("when repository has multiple existing lists, new list is added with isSelected=false") {
            val existingLists = listOf(
                ChannelList(id = 1, listName = "List A", isSelected = true),
                ChannelList(id = 2, listName = "List B", isSelected = false)
            )
            val capturedList = slot<ChannelList>()
            coEvery { channelListRepository.loadChannelsLists() } returns existingLists
            coEvery { channelListRepository.addChannelList(capture(capturedList)) } just Runs

            useCase("List C")

            capturedList.captured.listName shouldBe "List C"
            capturedList.captured.isSelected shouldBe false

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelList(any()) }
            confirmVerified(channelListRepository)
        }
    }
})
