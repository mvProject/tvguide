package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddPlaylistUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeletePlaylistUseCase
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.UserCustomListViewModel
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.action.UserListAction
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow

class UserCustomListViewModelTest : StringSpec({
    lateinit var customListRepository: CustomListRepository
    lateinit var addPlaylistUseCase: AddPlaylistUseCase
    lateinit var deletePlaylistUseCase: DeletePlaylistUseCase
    lateinit var userCustomListViewModel: UserCustomListViewModel

    beforeTest {
        customListRepository = mockk<CustomListRepository>()
        addPlaylistUseCase = mockk<AddPlaylistUseCase>()
        deletePlaylistUseCase = mockk<DeletePlaylistUseCase>()
        userCustomListViewModel =
            UserCustomListViewModel(customListRepository, addPlaylistUseCase, deletePlaylistUseCase)
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    "viewmodel calls" {
        withClue("viewmodel init calls") {
            userCustomListViewModel.customs.value shouldBe emptyList()
            userCustomListViewModel.customs.value shouldNotBe null
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

        userCustomListViewModel.processAction(UserListAction.AddList("test"))

        coVerify(exactly = 1) {
            addPlaylistUseCase.invoke("test")
        }
    }

    "action delete called" {
        coEvery {
            customListRepository.loadChannelsLists()
        } answers {
            flow {
                emit(listOf())
            }
        }

        userCustomListViewModel.processAction(
            UserListAction.DeleteList(
                UserChannelsList(
                    1,
                    "test",
                ),
            ),
        )

        coVerify(exactly = 1) {
            deletePlaylistUseCase.invoke(UserChannelsList(1, "test"))
            customListRepository.loadChannelsLists()
        }
    }
})
