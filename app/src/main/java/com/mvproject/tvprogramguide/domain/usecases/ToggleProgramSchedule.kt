package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import javax.inject.Inject
import kotlin.random.Random

/**
 * Use case for toggling the schedule state of a program.
 *
 * @property programRepository The repository for managing program data.
 * @property programSchedulerHelper The helper for scheduling program alarms.
 */
class ToggleProgramSchedule
@Inject
constructor(
    private val programRepository: ProgramRepository,
    private val programSchedulerHelper: ProgramSchedulerHelper,
) {
    /**
     * Toggles the schedule state of a program, either scheduling it or canceling an existing schedule.
     *
     * This function performs the following steps:
     * 1. Checks if the program is already scheduled.
     * 2. If not scheduled, creates a new schedule with a random ID and sets an alarm.
     * 3. If already scheduled, cancels the existing alarm and removes the schedule.
     * 4. Updates the program in the repository with the new schedule state.
     *
     * @param channelName The name of the channel the program belongs to.
     * @param program The Program object to toggle the schedule for.
     * @return The scheduledId of the program if scheduled, null if unscheduled.
     */
    suspend operator fun invoke(
        channelName: String,
        program: Program,
    ): Long? {
        val selectedProgram =
            if (program.scheduledId == null) {
                // Program is not scheduled, create a new schedule
                val id = Random.nextLong()
                val scheduled = program.copy(scheduledId = id)
                programSchedulerHelper.scheduleProgramAlarm(
                    programSchedule = scheduled,
                    channelName = channelName,
                )
                scheduled
            } else {
                // Program is already scheduled, cancel the alert
                val idForCancel = program.scheduledId
                programSchedulerHelper.cancelProgramAlarm(schedulerId = idForCancel)
                program.copy(scheduledId = null)
            }
        programRepository.updateProgram(program = selectedProgram)
        return selectedProgram.scheduledId
    }
}
