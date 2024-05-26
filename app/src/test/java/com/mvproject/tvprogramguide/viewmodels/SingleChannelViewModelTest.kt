package com.mvproject.tvprogramguide.viewmodels

// class SingleChannelViewModelTest : StringSpec({
/*    val channelName = "channelName"
    val program = Program(0, 0, "test", "titleProgram")

    lateinit var savedStateHandle: SavedStateHandle
    lateinit var getProgramsByChannel: GetProgramsByChannel
    lateinit var toggleProgramSchedule: ToggleProgramSchedule
    lateinit var singleChannelViewModel: SingleChannelViewModel

    beforeTest {
        getProgramsByChannel = mockk<GetProgramsByChannel>()
        toggleProgramSchedule = mockk<ToggleProgramSchedule>()
        savedStateHandle = mockk<SavedStateHandle>()
        singleChannelViewModel =
            SingleChannelViewModel(
                savedStateHandle = savedStateHandle,
                getProgramsByChannel = getProgramsByChannel,
                toggleProgramSchedule = toggleProgramSchedule,
            )
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    "viewmodel calls" {
        withClue("viewmodel init calls") {
            singleChannelViewModel.selectedPrograms.value shouldBe emptyList()
            singleChannelViewModel.selectedPrograms.value shouldNotBe null
        }
    }

    "retrieve channels" {
        coEvery {
            getProgramsByChannel
                .invoke("test")
        } returns listOf()

        singleChannelViewModel.loadPrograms("test")

        coVerify(exactly = 1) {
            getProgramsByChannel
                .invoke("test")
        }

        singleChannelViewModel.selectedPrograms.value.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
    }

    "schedule channel and update" {
        coEvery {
            toggleProgramSchedule.invoke(channelName, program)
        } just runs

        singleChannelViewModel.toggleSchedule(channelName, program)

        coVerify(exactly = 1) {
            // sortedProgramsUseCase.updateProgramScheduleWithAlarm(program)
            getProgramsByChannel
                .invoke("test")
        }

        singleChannelViewModel.selectedPrograms.value.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
    }*/
// })
