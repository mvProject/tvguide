package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify

class ToggleProgramScheduleUseCaseTest : FunSpec({

    lateinit var programRepository: IProgramRepository
    lateinit var programSchedulerHelper: ProgramSchedulerHelper
    lateinit var useCase: ToggleProgramScheduleUseCase

    beforeTest {
        programRepository = mockk()
        programSchedulerHelper = mockk()
        useCase = ToggleProgramScheduleUseCase(
            programRepository = programRepository,
            programSchedulerHelper = programSchedulerHelper,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("when program scheduledId is null, scheduleProgramAlarm is called and updateProgram is called with a non-null scheduledId") {
            val program = Program(
                programId = "prog1",
                dateTimeStart = 1_000_000L,
                dateTimeEnd = 2_000_000L,
                title = "Test Show",
                channel = "ch1",
                scheduledId = null,
            )
            val channelName = "Channel One"
            val savedProgramSlot = slot<Program>()

            every {
                programSchedulerHelper.scheduleProgramAlarm(
                    any(),
                    channelName = any()
                )
            } just Runs
            coEvery { programRepository.updateProgram(program = capture(savedProgramSlot)) } just Runs

            val result = useCase(channelName = channelName, program = program)

            // Return value should be the generated scheduledId (non-null)
            result.shouldNotBeNull()
            result shouldBeGreaterThan 0L

            // The saved program should carry the new scheduledId
            val saved = savedProgramSlot.captured
            saved.scheduledId.shouldNotBeNull()
            saved.scheduledId shouldBe result

            // scheduleProgramAlarm must be called with the program that has scheduledId set
            verify(exactly = 1) {
                programSchedulerHelper.scheduleProgramAlarm(
                    programSchedule = saved,
                    channelName = channelName,
                )
            }
            coVerify(exactly = 1) { programRepository.updateProgram(program = saved) }
            verify(exactly = 0) { programSchedulerHelper.cancelProgramAlarm(any()) }
            confirmVerified(programRepository, programSchedulerHelper)
        }

        test("when program scheduledId is not null, cancelProgramAlarm is called and updateProgram is called with scheduledId null") {
            val existingScheduledId = 99_999L
            val program = Program(
                programId = "prog2",
                dateTimeStart = 1_000_000L,
                dateTimeEnd = 2_000_000L,
                title = "Scheduled Show",
                channel = "ch2",
                scheduledId = existingScheduledId,
            )
            val channelName = "Channel Two"
            val savedProgramSlot = slot<Program>()

            every { programSchedulerHelper.cancelProgramAlarm(schedulerId = existingScheduledId) } just Runs
            coEvery { programRepository.updateProgram(program = capture(savedProgramSlot)) } just Runs

            val result = useCase(channelName = channelName, program = program)

            // Return value should be null (schedule cancelled)
            result.shouldBeNull()

            // The saved program should have scheduledId cleared
            val saved = savedProgramSlot.captured
            saved.scheduledId.shouldBeNull()

            verify(exactly = 1) { programSchedulerHelper.cancelProgramAlarm(schedulerId = existingScheduledId) }
            coVerify(exactly = 1) { programRepository.updateProgram(program = saved) }
            verify(exactly = 0) {
                programSchedulerHelper.scheduleProgramAlarm(
                    any(),
                    channelName = any()
                )
            }
            confirmVerified(programRepository, programSchedulerHelper)
        }

        test("scheduledId is derived from dateTimeStart + dateTimeEnd + title.hashCode + channel.hashCode") {
            val dateTimeStart = 1_000_000L
            val dateTimeEnd = 2_000_000L
            val title = "My Show"
            val channel = "ch3"
            val program = Program(
                programId = "prog3",
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                channel = channel,
                scheduledId = null,
            )
            val channelName = "Channel Three"
            val savedProgramSlot = slot<Program>()

            every {
                programSchedulerHelper.scheduleProgramAlarm(
                    any(),
                    channelName = any()
                )
            } just Runs
            coEvery { programRepository.updateProgram(program = capture(savedProgramSlot)) } just Runs

            val result = useCase(channelName = channelName, program = program)

            val expectedId = dateTimeStart + dateTimeEnd + title.hashCode() + channel.hashCode()
            result shouldBe expectedId
            savedProgramSlot.captured.scheduledId shouldBe expectedId

            val saved = savedProgramSlot.captured
            verify(exactly = 1) {
                programSchedulerHelper.scheduleProgramAlarm(
                    programSchedule = saved,
                    channelName = channelName,
                )
            }
            coVerify(exactly = 1) { programRepository.updateProgram(program = saved) }
            verify(exactly = 0) { programSchedulerHelper.cancelProgramAlarm(any()) }
            confirmVerified(programRepository, programSchedulerHelper)
        }
    }
})
