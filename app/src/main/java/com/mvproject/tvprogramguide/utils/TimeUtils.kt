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

/**
 * Utility object providing helper functions for time-related operations.
 */
object TimeUtils {
    private val tzSourceBerlin = TimeZone.of("Europe/Berlin")
    private val tzSourceMoscow = TimeZone.of("Europe/Moscow")
    private val tzCurrent = TimeZone.currentSystemDefault()

    /**
     * Gets the current epoch time in milliseconds.
     */
    val actualDate
        get() =
            Clock.System
                .now()
                .toEpochMilliseconds()

    /**
     * Date format for parsing and formatting dates in the format dd/MM/yyyy.
     */
    private val dateFormat =
        LocalDate.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        }

    /**
     * Time format for parsing and formatting times in the format HH:mm.
     */
    private val timeFormat =
        LocalTime.Format {
            hour()
            char(':')
            minute()
        }

    /**
     * Calculates the progress of a program based on its start and end times.
     *
     * @param startTime The start time of the program in milliseconds.
     * @param endTime The end time of the program in milliseconds.
     * @return A float value representing the progress (0.0 to 1.0).
     */
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

    /**
     * Extension function to correct the time zone of a ProgramEntity.
     *
     * @receiver ProgramEntity The program entity to be corrected.
     * @return A new ProgramEntity with corrected time zone.
     */
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
    /*
        fun parseDateTime(input: String): Pair<Long, Long> {
            // val test = "14/09/2024 18:27-20:19 (112)"

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
        }*/
    /**
     * Rounds a time string to the nearest 5 minutes.
     *
     * @param time The time string in format "HH:mm".
     * @return A rounded time string in format "HH:mm".
     */
    @SuppressLint("DefaultLocale")
    fun roundTimeString(time: String): String {
        val (hour, minute) = time.split(":").map { it.toInt() }
        val lastDigit = minute % 10

        if (hour > 23) {
            throw IllegalArgumentException()
        } else {
            if (minute >= 57) {
                return String.format(
                    "%02d:00",
                    (hour + 1) % 24
                ) // Edge case for 57, 58, and 59 minutes
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

    /**
     * Extracts the date from a string in format "yyyyMMddHHmm".
     *
     * @param input The input string.
     * @return A date string in format "dd/MM/yyyy".
     */
    @SuppressLint("DefaultLocale")
    fun extractDate(input: String): String {
        val year = input.substring(0, 4).toInt()
        val month = input.substring(4, 6).toInt()
        val day = input.substring(6, 8).toInt()
        return String.format("%02d/%02d/%04d", day, month, year)
    }

    /**
     * Extracts the time from a string in format "yyyyMMddHHmm".
     *
     * @param input The input string.
     * @return A time string in format "HH:mm".
     */
    @SuppressLint("DefaultLocale")
    fun extractTime(input: String): String {
        val hour = input.substring(8, 10).toInt()
        val minute = input.substring(10, 12).toInt()
        return String.format("%02d:%02d", hour, minute)
    }

    /**
     * Parses a date-time string to an Instant (epoch milliseconds).
     *
     * @param input The input string in format "yyyyMMddHHmm".
     * @return The parsed time as epoch milliseconds.
     */
    fun parseToInstant(input: String): Long {
        val date = extractDate(input)
        val time = extractTime(input)

        val localDate = dateFormat.parse(date)
        val localTime = timeFormat.parse(roundTimeString(time = time))

        val localDateTime = LocalDateTime(localDate, localTime)
        return localDateTime.toInstant(tzSourceMoscow).toEpochMilliseconds()
    }
}


