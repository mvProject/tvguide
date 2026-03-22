package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.domain.Program

interface IProgramScheduler {
    fun scheduleProgramAlarm(programSchedule: Program, channelName: String)
    fun cancelProgramAlarm(schedulerId: Long)
}
