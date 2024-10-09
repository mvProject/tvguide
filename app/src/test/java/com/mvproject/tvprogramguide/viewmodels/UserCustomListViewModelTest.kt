package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.ChannelListViewModel
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.action.ChannelListAction
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow

class UserCustomListViewModelTest : StringSpec({
    lateinit var channelListRepository: ChannelListRepository
    lateinit var addChannelListUseCase: AddChannelListUseCase
    lateinit var deleteChannelListUseCase: DeleteChannelListUseCase
    lateinit var channelListViewModel: ChannelListViewModel

    beforeTest {
        channelListRepository = mockk<ChannelListRepository>()
        addChannelListUseCase = mockk<AddChannelListUseCase>()
        deleteChannelListUseCase = mockk<DeleteChannelListUseCase>()
        channelListViewModel =
            ChannelListViewModel(channelListRepository, addChannelListUseCase, deleteChannelListUseCase)
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    "viewmodel calls" {
        withClue("viewmodel init calls") {
            channelListViewModel.customs.value shouldBe emptyList()
            channelListViewModel.customs.value shouldNotBe null
        }
    }

    "action add called" {
        //  coEvery {
        //      customListRepository.loadChannelsLists()
        //  } answers {
        //      flow {
        //          emit(listOf())
        //      }
        //  }

        channelListViewModel.processAction(ChannelListAction.AddList("test"))

        coVerify(exactly = 1) {
            addChannelListUseCase.invoke("test")
        }
    }

    "action delete called" {
        coEvery {
            channelListRepository.loadChannelsListsAsFlow()
        } answers {
            flow {
                emit(listOf())
            }
        }

        channelListViewModel.processAction(
            ChannelListAction.DeleteList(
                ChannelList(
                    1,
                    "test",
                    false
                ),
            ),
        )

        coVerify(exactly = 1) {
            deleteChannelListUseCase.invoke(ChannelList(1, "test", false))
            channelListRepository.loadChannelsLists()
        }
    }
})
