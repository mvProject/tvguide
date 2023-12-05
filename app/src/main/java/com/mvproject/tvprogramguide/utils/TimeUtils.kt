package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

object TimeUtils {
    private val tzSource = TimeZone.of("Europe/Moscow")
    private val tzCurrent = TimeZone.currentSystemDefault()

    val actualDay
        get() = Clock.System.now()
            .minus(1.days)
            .toEpochMilliseconds()
    val actualDate
        get() = Clock.System.now()
            .toEpochMilliseconds()

    fun calculateProgramProgress(startTime: Long, endTime: Long): Float {
        var progressValue = 0f
        val currTime = System.currentTimeMillis()
        if (currTime > startTime) {
            val endValue = (endTime - startTime).toInt()
            val spendValue = (currTime - startTime).toDouble()
            progressValue = (spendValue / endValue).toFloat()
        }
        return progressValue
    }

    fun ProgramEntity.correctTimeZone(): ProgramEntity {
        val startInstant = Instant.fromEpochMilliseconds(this.dateTimeStart)
        val endInstant = Instant.fromEpochMilliseconds(this.dateTimeEnd)
        return this.copy(
            dateTimeStart = startInstant.toLocalDateTime(tzCurrent)
                .toInstant(tzSource)
                .toEpochMilliseconds(),
            dateTimeEnd = endInstant.toLocalDateTime(tzCurrent)
                .toInstant(tzSource)
                .toEpochMilliseconds()
        )
    }
}
