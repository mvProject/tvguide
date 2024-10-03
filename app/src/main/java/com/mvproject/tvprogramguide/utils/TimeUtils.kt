package com.mvproject.tvprogramguide.utils

import android.annotation.SuppressLint
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

object TimeUtils {
    private val tzSourceBerlin = TimeZone.of("Europe/Berlin")
    private val tzSourceMoscow = TimeZone.of("Europe/Moscow")
    private val tzCurrent = TimeZone.currentSystemDefault()

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
        val startInstant = Instant.fromEpochMilliseconds(this.dateTimeStart)
        val endInstant = Instant.fromEpochMilliseconds(this.dateTimeEnd)

        val updatedDateTimeStart =
            startInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSourceMoscow)
                .toEpochMilliseconds()

        val updatedDateTimeEnd =
            endInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSourceMoscow)
                .toEpochMilliseconds()

        return this.copy(
            dateTimeStart =
            updatedDateTimeStart,
            dateTimeEnd =
            updatedDateTimeEnd,
        )
    }

    fun parseDateTime(input: String): Pair<Long, Long> {
        // val test = "14/09/2024 18:27-20:19 (112)"

        // Split the input string
        val dateFormat =
            LocalDate.Format {
                dayOfMonth()
                char('/')
                monthNumber()
                char('/')
                year()
            }

        val timeFormat =
            LocalTime.Format {
                hour()
                char(':')
                minute()
            }

        val (dateTimeStart, durationPart) = input.split(" (")
        val (date, time) = dateTimeStart.split(" ")
        val (start, end) = time.split("-")
        val duration = durationPart.removeSuffix(")").toDouble().toInt()
        val localDate = dateFormat.parse(date)
        val startLocalTime = timeFormat.parse(roundTimeString(time = start))

        val startDateTime =
            LocalDateTime(
                localDate,
                startLocalTime,
            )

        val startInstant = startDateTime.toInstant(tzSourceBerlin)
        val endInstant = startInstant.plus(duration.minutes)

        return Pair(startInstant.toEpochMilliseconds(), endInstant.toEpochMilliseconds())
    }

    @SuppressLint("DefaultLocale")
    fun roundTimeString(time: String): String {
        val (hour, minute) = time.split(":").map { it.toInt() }
        val lastDigit = minute % 10

        if (minute >= 57) {
            return String.format("%02d:00", (hour + 1) % 24) // Edge case for 57, 58, and 59 minutes
        }

        val roundedMinute =
            when (lastDigit) {
                in 1..3 -> (minute / 10) * 10 // Rounds down (21 -> 20)
                in 4..6 -> ((minute / 10) * 10) + 5 // Rounds to 5 (24, 26 -> 25)
                in 7..9 -> ((minute / 10) + 1) * 10 // Rounds up (27, 29 -> 30)
                else -> minute // If the last digit is 0, keep it unchanged
            }

        // Ensure the rounded minute is formatted properly with leading zero if needed
        return String.format("%02d:%02d", hour, roundedMinute)
    }
}
