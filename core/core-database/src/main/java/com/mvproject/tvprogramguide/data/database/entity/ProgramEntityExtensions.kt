package com.mvproject.tvprogramguide.data.database.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private val tzSourceMoscow = TimeZone.of("Europe/Moscow")
private val tzCurrent = TimeZone.currentSystemDefault()

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
        dateTimeStart = updatedDateTimeStart,
        dateTimeEnd = updatedDateTimeEnd,
    )
}
