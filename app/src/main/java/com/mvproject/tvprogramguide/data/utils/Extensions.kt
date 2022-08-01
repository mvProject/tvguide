package com.mvproject.tvprogramguide.data.utils

import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"

private const val TARGET_DATE_FORMAT = "dd MM yyyy"
private const val TARGET_TIME_FORMAT = "HH:mm"
private const val TARGET_TIME_FORMAT_HOURS = "HH"
private const val TARGET_TIME_FORMAT_MINUTES = "mm"

fun String.parseChannelName(): String {
    if (this.contains(" • ")) {
        return this.split(" • ")[0]
    }
    return this
}

fun String.toMillis(): Long {
    val parser = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return parser.parse(this)?.time ?: -1L
}

/**
 * Extension Method to non-null long variable which
 * convert value to specified date pattern
 * @return String converted date value
 */
fun Long.convertDateToReadableFormat(): String {
    return SimpleDateFormat(
        TARGET_DATE_FORMAT,
        Locale.getDefault()
    ).format(this)
}

/**
 * Extension Method to non-null long variable which
 * convert value to specified date pattern
 * @return String converted date value
 */
fun Long.convertTimeToReadableFormat(): String {
    return SimpleDateFormat(
        TARGET_TIME_FORMAT,
        Locale.getDefault()
    ).format(this)
}

fun Long.toHoursFormat(): String {
    return SimpleDateFormat(
        TARGET_TIME_FORMAT_HOURS,
        Locale.getDefault()
    ).format(this)
}

fun Long.toMinutesFormat(): String {
    return SimpleDateFormat(
        TARGET_TIME_FORMAT_MINUTES,
        Locale.getDefault()
    ).format(this)
}

// todo find proper way for summer/winter time correction
fun Long.correctTimeZone(): Long {
    // val offset = (TimeZone.getDefault()?.rawOffset?.let {
    //    DEFAULT_TIME_ZONE_OFFSET - it
    // } ?: 0).toLong()
    // return this - offset
    return this
}

fun List<String>.obtainIndexOrZero(target: String): Int {
    val index = this.indexOf(target)
    return if (index > AppConstants.COUNT_ZERO) index else AppConstants.COUNT_ZERO
}

fun List<AvailableChannelResponse>.filterNoEpg() =
    this.filterNot {
        it.channelNames.contains("No Epg", true) ||
                it.channelNames.contains("Заглушка", true)
    }