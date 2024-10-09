package com.mvproject.tvprogramguide.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.utils.AppConstants.CHANNEL_NAME_NO_EPG_FILTER
import com.mvproject.tvprogramguide.utils.AppConstants.CHANNEL_NAME_PARSE_DELIMITER
import com.mvproject.tvprogramguide.utils.AppConstants.CHANNEL_NAME_PLUG_FILTER
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.NO_END_PROGRAM_DURATION
import com.mvproject.tvprogramguide.utils.AppConstants.NO_EPG_PROGRAM_DURATION
import com.mvproject.tvprogramguide.utils.AppConstants.NO_EPG_PROGRAM_RANGE_END
import com.mvproject.tvprogramguide.utils.AppConstants.NO_EPG_PROGRAM_RANGE_START
import com.mvproject.tvprogramguide.utils.AppConstants.NO_EPG_PROGRAM_TITLE
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_LONG
import com.mvproject.tvprogramguide.utils.AppConstants.USER_LIST_MAX_LENGTH
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.hours

private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
private const val DATE_FORMAT_SLASH = "dd/MM/yyyy HH:mm"
private const val TARGET_DATE_FORMAT = "dd MM yyyy"

/**
 * Extension Method to non-null string variable which
 * parse value with specified delimiter
 *
 * @return first entry from parse result or source if no matches
 */
fun String.parseChannelName(): String {
    if (this.contains(CHANNEL_NAME_PARSE_DELIMITER)) {
        return this.split(CHANNEL_NAME_PARSE_DELIMITER).first()
    }
    return this
}

/**
 * Extension Method to non-null string variable which
 * convert date value to long value in milliseconds
 *
 * @return long value
 */
fun String.toMillis(): Long {
    val parser = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return parser.parse(this)?.time ?: NO_VALUE_LONG
}

fun String.toMillisSlashed(): Long {
    val parser = SimpleDateFormat(DATE_FORMAT_SLASH, Locale.getDefault())
    return parser.parse(this)?.time ?: NO_VALUE_LONG
}

/**
 * Extension Method to non-null long variable which
 * convert value to specified date pattern
 *
 * @return String converted date value
 */
fun Long.convertDateToReadableFormat(): String =
    SimpleDateFormat(
        TARGET_DATE_FORMAT,
        Locale.getDefault(),
    ).format(this)

/**
 * Extension Method to non-null long variable which
 * convert value to specified time with local timezone
 *
 * @return String converted time value
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
 * Extension Method to non-null list of strings which
 * obtain index of target string in list
 *
 * @param target target to find
 *
 * @return index of target
 */
fun List<String>.obtainIndexOrZero(target: String): Int {
    val index = this.indexOf(target)
    return if (index > COUNT_ZERO) index else COUNT_ZERO
}

/**
 * Extension Method to non-null list of responses which
 * filter value for names with no epg
 *
 * @return filtered list of responses
 */
fun List<AvailableChannelResponse>.filterNoEpg() =
    this.filterNot {
        it.channelNames.contains(CHANNEL_NAME_NO_EPG_FILTER, true) ||
            it.channelNames.contains(CHANNEL_NAME_PLUG_FILTER, true)
    }

/**
 * Extension Method to non-null string variable which
 * create list od dummy program data with source string as channel id
 *
 * @return list of dummy program entities
 */
fun String.getNoProgramData(): List<ProgramEntity> =
    buildList {
        val initTime = Clock.System.now()
        for (i in NO_EPG_PROGRAM_RANGE_START..NO_EPG_PROGRAM_RANGE_END) {
            val startDelta = i * NO_EPG_PROGRAM_DURATION
            val start =
                (initTime + startDelta.hours)
                    .toEpochMilliseconds()
            val end =
                (initTime + (startDelta + NO_EPG_PROGRAM_DURATION).hours)
                    .toEpochMilliseconds()
            add(
                ProgramEntity(
                    dateTimeStart = start,
                    dateTimeEnd = end,
                    title = NO_EPG_PROGRAM_TITLE,
                    channelId = this@getNoProgramData,
                ),
            )
        }
    }

fun String.toNoProgramData(): List<Program> =
    buildList {
        val initTime = Clock.System.now()
        for (i in NO_EPG_PROGRAM_RANGE_START..NO_EPG_PROGRAM_RANGE_END) {
            val startDelta = i * NO_EPG_PROGRAM_DURATION
            val start =
                (initTime + startDelta.hours)
                    .toEpochMilliseconds()
            val end =
                (initTime + (startDelta + NO_EPG_PROGRAM_DURATION).hours)
                    .toEpochMilliseconds()
            add(
                Program(
                    dateTimeStart = start,
                    dateTimeEnd = end,
                    title = NO_EPG_PROGRAM_TITLE,
                    channel = this@toNoProgramData,
                ),
            )
        }
    }

/**
 * Obtain time values of program end from start time of next
 *
 * @return the list of long values in milliseconds
 */
fun List<String>.calculateEndings(): List<Long> =
    buildList {
        this@calculateEndings.zipWithNext().forEach { timing ->
            add(timing.second.toMillis())
        }
    }

/**
 * Obtain time value of program end for last element
 * with hour duration from start time of current
 *
 * @return the long value in milliseconds
 */
fun Long.getLastItemEnding() =
    (Instant.fromEpochMilliseconds(this) + NO_END_PROGRAM_DURATION.hours)
        .toEpochMilliseconds()

/**
 * Take elements from source only if count bigger than 0
 *
 * @param count count of elements for take
 *
 * @return list of elements
 */
fun <T> List<T>.takeIfCountNotEmpty(count: Int): List<T> =
    if (count <= COUNT_ZERO || count >= size) {
        this
    } else {
        take(count)
    }

fun String.manageLength() =
    if (this.length > USER_LIST_MAX_LENGTH) {
        this.substring(COUNT_ZERO until USER_LIST_MAX_LENGTH)
    } else {
        this
    }

fun String.trimSpaces() =
    this.replace(" ", "")

fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("no activity")
}
