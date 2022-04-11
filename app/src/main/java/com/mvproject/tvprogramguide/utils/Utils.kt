package com.mvproject.tvprogramguide.utils

import android.content.Context
import android.content.ContextWrapper
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"

private const val TARGET_DATE_FORMAT = "dd MM yyyy"
private const val TARGET_TIME_FORMAT = "HH:mm"
private const val TARGET_TIME_FORMAT_HOURS = "HH"
private const val TARGET_TIME_FORMAT_MINUTES = "mm"

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

    val actualDay = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS

    fun Context.pxToDp(px: Float): Int {
        return (px.toInt() / resources.displayMetrics.density).toInt()
    }

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

    fun Context.findActivity(): AppCompatActivity? = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    fun List<String>.obtainIndexOrZero(target: String): Int {
        val index = this.indexOf(target)
        return if (index > COUNT_ZERO) index else COUNT_ZERO
    }
}
