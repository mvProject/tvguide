package com.mvproject.tvprogramguide.domain.utils

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import com.mvproject.tvprogramguide.domain.workers.PartiallyUpdateProgramsWorker

const val NOTIFICATION_CONDITION = "NOTIFY"
const val DOWNLOAD_CHANNELS = "DOWNLOAD_CHANNELS"
const val DOWNLOAD_PROGRAMS = "DOWNLOAD_PROGRAMS"
const val CHANNEL_INDEX = "CHANNEL_INDEX"
const val CHANNEL_COUNT = "CHANNEL_COUNT"
const val CHANNEL_MISSING_COUNT = "CHANNEL_MISSING_COUNT"

fun buildPartiallyUpdateRequest(ids: Array<String>) = OneTimeWorkRequest
    .Builder(PartiallyUpdateProgramsWorker::class.java)
    .setInputData(createInputDataForPartialUpdate(ids = ids))
    .build()

fun buildFullUpdateRequest() = OneTimeWorkRequest
    .Builder(FullUpdateProgramsWorker::class.java)
    .setInputData(createInputDataForUpdate())
    .build()

fun createInputDataForPartialUpdate(isNotificationOn: Boolean = true, ids: Array<String>): Data {
    return Data.Builder().apply {
        putStringArray(CHANNEL_MISSING_COUNT, ids)
        putBoolean(NOTIFICATION_CONDITION, isNotificationOn)
    }.build()
}

fun createInputDataForUpdate(isNotificationOn: Boolean = true): Data {
    return Data.Builder().apply {
        putBoolean(NOTIFICATION_CONDITION, isNotificationOn)
    }.build()
}
