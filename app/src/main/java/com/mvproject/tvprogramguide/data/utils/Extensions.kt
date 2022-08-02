package com.mvproject.tvprogramguide.data.utils

import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_EPG_PROGRAM_DURATION
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_EPG_PROGRAM_RANGE_END
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_EPG_PROGRAM_RANGE_START
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_EPG_PROGRAM_TITLE
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_LONG
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.hours

private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
private const val TARGET_DATE_FORMAT = "dd MM yyyy"

fun String.parseChannelName(): String {
    if (this.contains(" • ")) {
        return this.split(" • ").first()
    }
    return this
}

fun String.toMillis(): Long {
    val parser = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return parser.parse(this)?.time ?: NO_VALUE_LONG
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
 * convert value to specified time with local timezone
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

fun List<String>.obtainIndexOrZero(target: String): Int {
    val index = this.indexOf(target)
    return if (index > AppConstants.COUNT_ZERO) index else AppConstants.COUNT_ZERO
}

fun List<AvailableChannelResponse>.filterNoEpg() =
    this.filterNot {
        it.channelNames.contains("No Epg", true) ||
                it.channelNames.contains("Заглушка", true)
    }

fun String.getNoProgramData(): List<ProgramEntity> {
    return buildList {
        val initTime = Clock.System.now()
        for (i in NO_EPG_PROGRAM_RANGE_START..NO_EPG_PROGRAM_RANGE_END) {
            val startDelta = i * NO_EPG_PROGRAM_DURATION
            val start = (initTime + startDelta.hours)
                .toEpochMilliseconds()
            val end = (initTime + (startDelta + NO_EPG_PROGRAM_DURATION).hours)
                .toEpochMilliseconds()
            add(
                ProgramEntity(
                    dateTimeStart = start,
                    dateTimeEnd = end,
                    title = NO_EPG_PROGRAM_TITLE,
                    channelId = this@getNoProgramData
                )
            )
        }
    }
}