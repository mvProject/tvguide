package com.mvproject.tvprogramguide.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannel
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel.SingleChannelViewModel
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class SingleChannelViewModelTest : StringSpec({
    val channelName = "channelName"
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
    }
})
