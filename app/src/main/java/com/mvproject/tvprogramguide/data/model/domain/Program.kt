package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.TimeUtils

@Immutable
data class Program(
    val programId:String,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
    val category: String = String.empty,
    val channel: String = String.empty,
    val scheduledId: Long? = null,
) {
    val programProgress
        get() =
            TimeUtils.calculateProgramProgress(
                startTime = dateTimeStart,
                endTime = dateTimeEnd,
            )
}
