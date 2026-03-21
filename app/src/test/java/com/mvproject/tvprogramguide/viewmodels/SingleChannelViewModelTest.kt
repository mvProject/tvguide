package com.mvproject.tvprogramguide.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannelUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelViewModel
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SingleChannelViewModelTest : FunSpec({
    val testChannelId = "testChannelId"
    val testChannelName = "testChannelName"

    lateinit var getProgramsByChannelUseCase: GetProgramsByChannelUseCase
    lateinit var toggleProgramScheduleUseCase: ToggleProgramScheduleUseCase
    lateinit var savedStateHandle: SavedStateHandle
    lateinit var singleChannelViewModel: SingleChannelViewModel

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        getProgramsByChannelUseCase = mockk<GetProgramsByChannelUseCase>()
        toggleProgramScheduleUseCase = mockk<ToggleProgramScheduleUseCase>()
        savedStateHandle = SavedStateHandle(
            mapOf(
                "channelId" to testChannelId,
                "channelName" to testChannelName,
            )
        )
        coEvery { getProgramsByChannelUseCase(channelId = testChannelId) } returns emptyList()
        singleChannelViewModel = SingleChannelViewModel(
            savedStateHandle = savedStateHandle,
            getProgramsByChannel = getProgramsByChannelUseCase,
            toggleProgramSchedule = toggleProgramScheduleUseCase,
        )
    }

    afterTest {
        unmockkAll()
        Dispatchers.resetMain()
    }

    test("initial selectedPrograms is empty") {
        withClue("selectedPrograms starts empty before coroutine completes") {
            singleChannelViewModel.selectedPrograms.isEmpty() shouldBe true
        }
    }

    test("name is read from savedStateHandle") {
        withClue("name matches value provided in SavedStateHandle") {
            singleChannelViewModel.name shouldBe testChannelName
        }
    }

    test("programs are loaded on init via use case") {
        runTest {
            val program = Program(
                programId = "p1",
                dateTimeStart = 0L,
                dateTimeEnd = 1000L,
                title = "Test Program",
                channel = testChannelId,
            )
            val expectedPrograms = listOf(
                SingleChannelWithPrograms(date = "2024-01-01", programs = listOf(program))
            )

            // Clear recorded calls from beforeTest VM init so coVerify(exactly=1) counts only this VM's call.
            clearMocks(getProgramsByChannelUseCase, answers = false)
            coEvery { getProgramsByChannelUseCase(channelId = testChannelId) } returns expectedPrograms

            val vm = SingleChannelViewModel(
                savedStateHandle = savedStateHandle,
                getProgramsByChannel = getProgramsByChannelUseCase,
                toggleProgramSchedule = toggleProgramScheduleUseCase,
            )

            advanceUntilIdle()

            coVerify(exactly = 1) { getProgramsByChannelUseCase(channelId = testChannelId) }
            withClue("selectedPrograms populated after init") {
                vm.selectedPrograms.size shouldBe expectedPrograms.size
            }
        }
    }

    test("toggleSchedule calls toggleProgramScheduleUseCase") {
        runTest {
            val program = Program(
                programId = "p1",
                dateTimeStart = 0L,
                dateTimeEnd = 1000L,
                title = "Test Program",
                channel = testChannelId,
            )
            val day = SingleChannelWithPrograms(date = "2024-01-01", programs = listOf(program))
            val scheduleId = 99L

            singleChannelViewModel.selectedPrograms.add(day)
            coEvery {
                toggleProgramScheduleUseCase(channelName = testChannelName, program = program)
            } returns scheduleId

            singleChannelViewModel.toggleSchedule(testChannelName, program)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                toggleProgramScheduleUseCase(channelName = testChannelName, program = program)
            }
        }
    }
})
