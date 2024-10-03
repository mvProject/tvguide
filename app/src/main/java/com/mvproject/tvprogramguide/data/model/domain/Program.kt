package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.TimeUtils

@Immutable
data class Program(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING,
    val channel: String = NO_VALUE_STRING,
    val scheduledId: Long? = null,
) {
    val programProgress
        get() =
            TimeUtils.calculateProgramProgress(
                startTime = dateTimeStart,
                endTime = dateTimeEnd,
            )

    val programKey get() = "$dateTimeStart$title"

    fun toEntity() =
        with(this) {
            ProgramEntity(
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                category = category,
                channelId = channel,
                scheduledId = scheduledId,
            )
        }
}
