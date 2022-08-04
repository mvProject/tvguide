package com.mvproject.tvprogramguide.data.model.schedule

data class ProgramSchedule(
    val channelId: String,
    val programTitle: String,
    val channelTitle: String = "",
    val scheduleId: Long = 0,
    val triggerTime: Long = 0
)