package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll

class SelectChannelListUseCaseTest : FunSpec({

    lateinit var channelListRepository: IChannelListRepository
    lateinit var useCase: SelectChannelListUseCase

    beforeTest {
        channelListRepository = mockk()
        useCase = SelectChannelListUseCase(channelListRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("only the target list gets isSelected=true, all others get isSelected=false") {
            val list1 = ChannelList(id = 1, listName = "List 1", isSelected = true)
            val list2 = ChannelList(id = 2, listName = "List 2", isSelected = false)
            val list3 = ChannelList(id = 3, listName = "List 3", isSelected = false)
            val capturedLists = slot<List<ChannelList>>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(
                list1,
                list2,
                list3
            )
            coEvery { channelListRepository.addChannelLists(capture(capturedLists)) } just Runs

            useCase(list2)

            capturedLists.captured shouldHaveSize 3
            capturedLists.captured.find { it.id == 1 }?.isSelected shouldBe false
            capturedLists.captured.find { it.id == 2 }?.isSelected shouldBe true
            capturedLists.captured.find { it.id == 3 }?.isSelected shouldBe false

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelLists(any()) }
            confirmVerified(channelListRepository)
        }

        test("selecting an already-selected list keeps it selected and others remain unselected") {
            val list1 = ChannelList(id = 1, listName = "List 1", isSelected = true)
            val list2 = ChannelList(id = 2, listName = "List 2", isSelected = false)
            val capturedLists = slot<List<ChannelList>>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(list1, list2)
            coEvery { channelListRepository.addChannelLists(capture(capturedLists)) } just Runs

            useCase(list1)

            capturedLists.captured shouldHaveSize 2
            capturedLists.captured.find { it.id == 1 }?.isSelected shouldBe true
            capturedLists.captured.find { it.id == 2 }?.isSelected shouldBe false

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelLists(any()) }
            confirmVerified(channelListRepository)
        }

        test("selecting a list when only one list exists marks it as selected") {
            val singleList = ChannelList(id = 1, listName = "Only List", isSelected = false)
            val capturedLists = slot<List<ChannelList>>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(singleList)
            coEvery { channelListRepository.addChannelLists(capture(capturedLists)) } just Runs

            useCase(singleList)

            capturedLists.captured shouldHaveSize 1
            capturedLists.captured.first().isSelected shouldBe true

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelLists(any()) }
            confirmVerified(channelListRepository)
        }

        test("when target list id is not present in loaded lists, all lists get isSelected=false") {
            val list1 = ChannelList(id = 1, listName = "List 1", isSelected = true)
            val list2 = ChannelList(id = 2, listName = "List 2", isSelected = false)
            val unknownList = ChannelList(id = 99, listName = "Unknown", isSelected = false)
            val capturedLists = slot<List<ChannelList>>()

            coEvery { channelListRepository.loadChannelsLists() } returns listOf(list1, list2)
            coEvery { channelListRepository.addChannelLists(capture(capturedLists)) } just Runs

            useCase(unknownList)

            capturedLists.captured shouldHaveSize 2
            capturedLists.captured.all { !it.isSelected } shouldBe true

            coVerify(exactly = 1) { channelListRepository.loadChannelsLists() }
            coVerify(exactly = 1) { channelListRepository.addChannelLists(any()) }
            confirmVerified(channelListRepository)
        }
    }
})
