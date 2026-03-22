package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardViewModel
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardViewModelTest : FunSpec({
    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var onBoardViewModel: OnBoardViewModel

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        preferenceRepository = mockk<IPreferenceRepository>()
        onBoardViewModel = OnBoardViewModel(preferenceRepository = preferenceRepository)
    }

    afterTest {
        unmockkAll()
        Dispatchers.resetMain()
    }

    test("completeOnBoard calls setOnBoardState with false") {
        coEvery {
            preferenceRepository.setOnBoardState(false)
        } just runs

        onBoardViewModel.completeOnBoard()

        coVerify(exactly = 1) {
            preferenceRepository.setOnBoardState(false)
        }
    }
})
