package com.mvproject.tvprogramguide.domain.utils

import androidx.work.Data
import timber.log.Timber

const val NOTIFICATION_CONDITION = "NOTIFY"
const val DOWNLOAD_CHANNELS = "DOWNLOAD_CHANNELS"
const val DOWNLOAD_FULL_PROGRAMS = "DOWNLOAD_FULL_PROGRAMS"
const val CHANNEL_INDEX = "CHANNEL_INDEX"
const val CHANNEL_COUNT = "CHANNEL_COUNT"
const val CHANNEL_MISSING_COUNT = "CHANNEL_MISSING_COUNT"

/**
 * Method for sleeping for a fixed about of time to emulate slower work
 */
fun sleep() {
    try {
        Thread.sleep(1000L, 0)
    } catch (e: InterruptedException) {
        Timber.e(e)
    }
}

fun createInputDataForPartialUpdate(isNotificationOn: Boolean = false, ids: Array<String>,): Data {
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
