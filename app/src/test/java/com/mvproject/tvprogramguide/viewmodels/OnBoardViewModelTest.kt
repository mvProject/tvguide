package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class OnBoardViewModelTest :
    StringSpec({
        lateinit var preferenceRepository: PreferenceRepository
        lateinit var onBoardViewModel: OnBoardViewModel

        beforeTest {
            preferenceRepository = mockk<PreferenceRepository>()
            onBoardViewModel = OnBoardViewModel(preferenceRepository)
        }

        afterTest {
            println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
        }

        "onboard set state true" {
            coEvery {
                preferenceRepository.setOnBoardState(any())
            } just runs

            withClue("onboard complete") {
                onBoardViewModel.completeOnBoard(true)

                coVerify(exactly = 1) {
                    preferenceRepository.setOnBoardState(true)
                }
            }
        }

        "onboard set state false" {
            coEvery {
                preferenceRepository.setOnBoardState(any())
            } just runs

            withClue("onboard complete") {
                onBoardViewModel.completeOnBoard(false)

                coVerify(exactly = 1) {
                    preferenceRepository.setOnBoardState(false)
                }
            }
        }
    })
