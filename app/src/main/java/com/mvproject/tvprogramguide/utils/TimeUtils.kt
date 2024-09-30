package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import kotlin.time.Duration.Companion.days

object TimeUtils {
    val tzSource1 = TimeZone.of("Europe/Berlin")
    val tzSource = TimeZone.of("Europe/Moscow")
    val tzCurrent = TimeZone.currentSystemDefault()

    val actualDay
        get() =
            Clock.System
                .now()
                .minus(1.days)
                .toEpochMilliseconds()
    val actualDate
        get() =
            Clock.System
                .now()
                .toEpochMilliseconds()

    fun calculateProgramProgress(
        startTime: Long,
        endTime: Long,
    ): Float {
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
        Timber.d("testing correctTimeZone title ${this.title}")
        Timber.d("testing correctTimeZone dateTimeStart ${this.dateTimeStart.convertTimeToReadableFormat()}")
        Timber.d("testing correctTimeZone dateTimeEnd ${this.dateTimeEnd.convertTimeToReadableFormat()}")
        val startInstant = Instant.fromEpochMilliseconds(this.dateTimeStart)
        val endInstant = Instant.fromEpochMilliseconds(this.dateTimeEnd)

        val updatedDateTimeStart =
            startInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSource)
                .toEpochMilliseconds()

        val updatedDateTimeEnd =
            endInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSource)
                .toEpochMilliseconds()

        Timber.d("testing correctTimeZone updatedDateTimeStart ${updatedDateTimeStart.convertTimeToReadableFormat()}")
        Timber.d("testing correctTimeZone updatedDateTimeEnd ${updatedDateTimeEnd.convertTimeToReadableFormat()}")

        return this.copy(
            dateTimeStart =
            updatedDateTimeStart,
            dateTimeEnd =
            updatedDateTimeEnd,
        )
    }
}
