package com.mvproject.tvprogramguide.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"

private const val TARGET_DATE_FORMAT = "dd MM yyyy"
private const val TARGET_TIME_FORMAT = "HH:mm"

private const val DEFAULT_TIME_ZONE_OFFSET = 10800000

object Utils {
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

    fun Long.correctTimeZone(): Long {
        val offset = (TimeZone.getDefault()?.rawOffset?.let {
            DEFAULT_TIME_ZONE_OFFSET - it
        } ?: 0).toLong()
        return this - offset
    }

    val actualDay = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS

}