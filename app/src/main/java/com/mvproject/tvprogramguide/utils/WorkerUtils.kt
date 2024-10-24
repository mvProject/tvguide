package com.mvproject.tvprogramguide.utils

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker

const val NOTIFICATION_CONDITION = "NOTIFY"
const val DOWNLOAD_PROGRAMS = "DOWNLOAD_PROGRAMS"
const val CHANNEL_INDEX = "CHANNEL_INDEX"
const val CHANNEL_COUNT = "CHANNEL_COUNT"
const val CHANNEL_MISSING_COUNT = "CHANNEL_MISSING_COUNT"

/**
 * Builds a OneTimeWorkRequest for a full update of programs.
 *
 * @return OneTimeWorkRequest configured for FullUpdateProgramsWorker.
 */
fun buildFullUpdateRequest() = OneTimeWorkRequest
    .Builder(FullUpdateProgramsWorker::class.java)
    .setInputData(createInputDataForUpdate())
    .build()

/**
 * Creates input data for the update work request.
 *
 * @param isNotificationOn Boolean flag to determine if notifications should be shown during the update.
 * @return Data object containing the input data for the work request.
 */
fun createInputDataForUpdate(isNotificationOn: Boolean = true): Data {
    return Data.Builder().apply {
        putBoolean(NOTIFICATION_CONDITION, isNotificationOn)
    }.build()
}
