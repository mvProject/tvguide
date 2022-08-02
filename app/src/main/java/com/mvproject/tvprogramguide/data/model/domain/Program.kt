package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.domain.utils.Utils

@Immutable
data class Program(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING,
    val channel: String = NO_VALUE_STRING
) {
    val programProgress
        get() = Utils.calculateProgramProgress(
            startTime = dateTimeStart,
            endTime = dateTimeEnd
        )
}
