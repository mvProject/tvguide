package com.mvproject.tvprogramguide.domain.utils

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.days

object Utils {
    private val tzSource = TimeZone.of("Europe/Moscow")
    private val tzCurrent = TimeZone.currentSystemDefault()

    val actualDay
        get() = Clock.System.now()
            .minus(1.days)
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

    fun Program.correctTimeZone(): Program {
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
