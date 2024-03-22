package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import javax.inject.Inject
import kotlin.random.Random

/**
 * Use case to update available channels
 * @property channelProgramRepository the ChannelProgramRepository repository
 * @property programSchedulerHelper the ProgramSchedulerHelper repository
 *
 */
class ToggleProgramSchedule
    @Inject
    constructor(
        private val channelProgramRepository: ChannelProgramRepository,
        private val programSchedulerHelper: ProgramSchedulerHelper,
    ) {
        /**
         * Update selected channel for mark as scheduled or cancel (if already was scheduled)
         *
         * @param program data object with channel info
         * @param channelName data object with channel info
         */
        suspend operator fun invoke(
            channelName: String,
            program: Program,
        ) {
            val selectedProgram =
                if (program.scheduledId == null) {
                    val id = Random.nextLong()
                    val scheduled = program.copy(scheduledId = id)
                    programSchedulerHelper.scheduleProgramAlarm(
                        programSchedule = scheduled,
                        channelName = channelName,
                    )
                    scheduled
                } else {
                    // cancel alert
                    val idForCancel = program.scheduledId
                    programSchedulerHelper.cancelProgramAlarm(schedulerId = idForCancel)
                    program.copy(scheduledId = null)
                }
            channelProgramRepository.updateProgram(program = selectedProgram)
        }
    }
