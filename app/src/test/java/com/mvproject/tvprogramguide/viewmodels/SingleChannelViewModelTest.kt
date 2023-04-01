package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.ui.singlechannel.viewmodel.SingleChannelViewModel
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*

class SingleChannelViewModelTest : StringSpec({
    val programSchedule = ProgramSchedule("test", "titleProgram")

    lateinit var sortedProgramsUseCase: SortedProgramsUseCase
    lateinit var singleChannelViewModel: SingleChannelViewModel

    beforeTest {
        sortedProgramsUseCase = mockk<SortedProgramsUseCase>()
        singleChannelViewModel = SingleChannelViewModel(sortedProgramsUseCase)
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
            sortedProgramsUseCase
                .retrieveProgramsForChannel("test")
        } returns listOf()

        singleChannelViewModel.loadPrograms("test")

        coVerify(exactly = 1) {
            sortedProgramsUseCase
                .retrieveProgramsForChannel("test")
        }

        singleChannelViewModel.selectedPrograms.value.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
    }

    "schedule channel and update" {
        coEvery {
            sortedProgramsUseCase
                .updateProgramScheduleWithAlarm(programSchedule)
        } just runs

        singleChannelViewModel.toggleProgramSchedule(programSchedule)

        coVerify(exactly = 1) {
            sortedProgramsUseCase.updateProgramScheduleWithAlarm(programSchedule)
            sortedProgramsUseCase
                .retrieveProgramsForChannel("test")
        }

        singleChannelViewModel.selectedPrograms.value.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
    }
})