@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.ui.screens.channellist.ChannelListViewModel
import com.mvproject.tvprogramguide.ui.screens.channellist.action.ChannelListAction
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class UserCustomListViewModelTest : StringSpec({
    lateinit var channelListRepository: ChannelListRepository
    lateinit var addChannelListUseCase: AddChannelListUseCase
    lateinit var deleteChannelListUseCase: DeleteChannelListUseCase
    lateinit var selectChannelListUseCase: SelectChannelListUseCase
    lateinit var channelListViewModel: ChannelListViewModel

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        channelListRepository = mockk<ChannelListRepository>()
        addChannelListUseCase = mockk<AddChannelListUseCase>()
        deleteChannelListUseCase = mockk<DeleteChannelListUseCase>()
        selectChannelListUseCase = mockk<SelectChannelListUseCase>()
        every { channelListRepository.loadChannelsListsAsFlow() } returns flowOf(emptyList())
        channelListViewModel = ChannelListViewModel(
            channelListRepository,
            addChannelListUseCase,
            deleteChannelListUseCase,
            selectChannelListUseCase
        )
    }

    afterTest {
        Dispatchers.resetMain()
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
        channelListViewModel.processAction(
            ChannelListAction.DeleteList(
                ChannelList(1, "test", false)
            )
        )

        coVerify(exactly = 1) {
            deleteChannelListUseCase.invoke(ChannelList(1, "test", false))
        }
    }
})
