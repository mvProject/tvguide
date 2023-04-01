package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel
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
    lateinit var preferenceRepository: PreferenceRepository
    lateinit var userCustomListViewModel: UserCustomListViewModel

    beforeTest {
        customListRepository = mockk<CustomListRepository>()
        preferenceRepository = mockk<PreferenceRepository>()
        userCustomListViewModel =
            UserCustomListViewModel(customListRepository, preferenceRepository)
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
        userCustomListViewModel.processAction(UserListAction.AddList("test"))

        coVerify(exactly = 1) {
            customListRepository.addCustomList("test")
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
                    "test"
                )
            )
        )

        coVerify(exactly = 1) {
            customListRepository.deleteList(UserChannelsList(1, "test"))
            customListRepository.loadChannelsLists()
        }
    }
})