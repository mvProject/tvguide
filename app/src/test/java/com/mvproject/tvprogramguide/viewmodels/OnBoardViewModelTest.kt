package com.mvproject.tvprogramguide.viewmodels

/*class OnBoardViewModelTest :
    StringSpec({
        lateinit var preferenceRepository: PreferenceRepository
        lateinit var onBoardViewModel: ChannelViewModel

        beforeTest {
            preferenceRepository = mockk<PreferenceRepository>()
            onBoardViewModel = ChannelViewModel(preferenceRepository)
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
    })*/
