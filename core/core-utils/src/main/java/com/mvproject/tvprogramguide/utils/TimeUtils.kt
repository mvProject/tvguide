package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.utils.AppConstants.empty
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Utility object providing helper functions for time-related operations.
 * Handles time zone conversions, date-time parsing, and formatting for the TV program guide.
 * Supports multiple time zones including Berlin, Moscow, and the system default.
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
     * Formats for parsing and formatting dates in dd/MM/yyyy pattern.
     * Used for consistent date handling throughout the application.
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
     * Formats for parsing and formatting times in HH:mm pattern.
     * Used for consistent time handling throughout the application.
     */
    private val timeFormat =
        LocalTime.Format {
            hour()
            char(':')
            minute()
        }

    /**
     * Calculates the current playback progress of a program.
     * Returns a value between 0.0 (not started) and 1.0 (completed).
     *
     * @param startTime Program start time in milliseconds
     * @param endTime Program end time in milliseconds
     * @return Float value representing progress percentage (0.0 to 1.0)
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
     * Rounds a time string to the nearest 5-minute interval.
     * Examples:
     * - "10:21" becomes "10:20"
     * - "10:24" becomes "10:25"
     * - "10:57" becomes "11:00"
     *
     * @param time Time string in "HH:mm" format
     * @return Rounded time string in "HH:mm" format
     * @throws IllegalArgumentException if hour value is greater than 23
     */
    fun roundTimeString(time: String): String {
        val (hour, minute) = time.split(":").map { it.toInt() }
        val lastDigit = minute % 10

        if (hour > 23) {
            throw IllegalArgumentException()
        } else {
            if (minute >= 57) {
                return "${
                    ((hour + 1) % 24).toString().padStart(2, '0')
                }:00" // Edge case for 57, 58, and 59 minutes
            }

            val roundedMinute =
                when (lastDigit) {
                    in 1..3 -> (minute / 10) * 10 // Rounds down (21 -> 20)
                    in 4..6 -> ((minute / 10) * 10) + 5 // Rounds to 5 (24, 26 -> 25)
                    in 7..9 -> ((minute / 10) + 1) * 10 // Rounds up (27, 29 -> 30)
                    else -> minute // If the last digit is 0, keep it unchanged
                }

            // Ensure the rounded minute is formatted properly with leading zero if needed
            return "${hour.toString().padStart(2, '0')}:${
                roundedMinute.toString().padStart(2, '0')
            }"
        }

    }

    /**
     * Extracts date components from a datetime string.
     *
     * @param input String in "yyyyMMddHHmm" format
     * @return Formatted date string in "dd/MM/yyyy" format
     */
    fun extractDate(input: String): String =
        "${input.substring(6, 8)}/${input.substring(4, 6)}/${input.substring(0, 4)}"

    /**
     * Extracts time components from a datetime string.
     *
     * @param input String in "yyyyMMddHHmm" format
     * @return Formatted time string in "HH:mm" format
     */
    fun extractTime(input: String): String =
        "${input.substring(8, 10)}:${input.substring(10, 12)}"

    /**
     * Converts a datetime string to epoch milliseconds.
     * Parses the input string and converts it to Moscow timezone.
     *
     * @param input String in "yyyyMMddHHmm" format
     * @return Epoch milliseconds in Moscow timezone
     */
    fun parseToInstant(input: String): Long {
        val date = extractDate(input)
        val time = extractTime(input)

        val localDate = dateFormat.parse(date)
        val localTime = timeFormat.parse(roundTimeString(time = time))

        val localDateTime = LocalDateTime(localDate, localTime)
        return localDateTime.toInstant(tzSourceMoscow).toEpochMilliseconds()
    }

    /**
     * Formats a timestamp to a human-readable date-time string.
     *
     * @receiver Long The timestamp in milliseconds
     * @return A formatted string in pattern "dd/MM/yyyy - HH:mm" or empty string if parsing fails
     */
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun Long.toFormattedDateTime(): String {
        return try {
            val instant = Instant.fromEpochMilliseconds(this)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            dateTime.format(
                LocalDateTime.Format {
                    byUnicodePattern("dd/MM/yyyy - HH:mm")
                }
            )
        } catch (e: Exception) {
            String.empty
        }
    }
}


