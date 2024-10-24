package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.USER_LIST_MAX_LENGTH
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

private const val TARGET_DATE_FORMAT = "dd MM yyyy"

/**
 * Converts a Long timestamp to a readable date format.
 *
 * @return A string representation of the date in the format "dd MM yyyy".
 */
fun Long.convertDateToReadableFormat(): String =
    SimpleDateFormat(
        TARGET_DATE_FORMAT,
        Locale.getDefault(),
    ).format(this)


/**
 * Converts a Long timestamp to a readable date format.
 *
 * @return A string representation of the date in the format "dd MM yyyy".
 */
fun Long.convertDateToReadableFormat2(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')} " +
            "${localDateTime.monthNumber.toString().padStart(2, '0')} " +
            "${localDateTime.year}"
}

/**
 * Converts a Long timestamp to a readable time format.
 *
 * @return A string representation of the time in the local time zone.
 */
fun Long.convertTimeToReadableFormat(): String {
    val targetTz = TimeZone.currentSystemDefault()
    return Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(targetTz)
        .time
        .toString()
}

/**
 * Finds the index of a target string in a list, returning 0 if not found.
 *
 * @param target The string to search for in the list.
 * @return The index of the target string, or 0 if not found.
 */
fun List<String>.obtainIndexOrZero(target: String): Int {
    val index = this.indexOf(target)
    return if (index > COUNT_ZERO) index else COUNT_ZERO
}

/**
 * Takes a specified number of elements from the list if the count is valid.
 *
 * @param count The number of elements to take.
 * @return A list containing the specified number of elements, or the original list if count is invalid.
 */
fun <T> List<T>.takeIfCountNotEmpty(count: Int): List<T> =
    if (count <= COUNT_ZERO || count >= size) {
        this
    } else {
        take(count)
    }

/**
 * Manages the length of a string, truncating it if it exceeds the maximum length.
 *
 * @return The original string if its length is within the limit, or a truncated version if it exceeds the limit.
 */
fun String.manageLength() =
    if (this.length > USER_LIST_MAX_LENGTH) {
        this.substring(COUNT_ZERO until USER_LIST_MAX_LENGTH)
    } else {
        this
    }

/**
 * Removes all spaces from a string.
 *
 * @return A new string with all spaces removed.
 */
fun String.trimSpaces() =
    this.replace(" ", "")