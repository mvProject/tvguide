package com.mvproject.tvprogramguide.utils

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker

const val NOTIFICATION_CONDITION = "NOTIFY"
const val DOWNLOAD_PROGRAMS = "DOWNLOAD_PROGRAMS"
const val CHANNEL_INDEX = "CHANNEL_INDEX"
const val CHANNEL_COUNT = "CHANNEL_COUNT"
const val CHANNEL_MISSING_COUNT = "CHANNEL_MISSING_COUNT"

fun buildFullUpdateRequest() = OneTimeWorkRequest
    .Builder(FullUpdateProgramsWorker::class.java)
    .setInputData(createInputDataForUpdate())
    .build()

fun createInputDataForUpdate(isNotificationOn: Boolean = true): Data {
    return Data.Builder().apply {
        putBoolean(NOTIFICATION_CONDITION, isNotificationOn)
    }.build()
}
