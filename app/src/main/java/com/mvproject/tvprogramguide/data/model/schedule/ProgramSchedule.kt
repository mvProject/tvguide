package com.mvproject.tvprogramguide.data.model.schedule

import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO_LONG
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING

data class ProgramSchedule(
    val channelId: String,
    val programTitle: String,
    val channelTitle: String = NO_VALUE_STRING,
    val scheduleId: Long = COUNT_ZERO_LONG,
    val triggerTime: Long = COUNT_ZERO_LONG
)