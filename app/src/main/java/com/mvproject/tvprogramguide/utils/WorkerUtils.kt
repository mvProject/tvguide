package com.mvproject.tvprogramguide.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import com.mvproject.tvprogramguide.R
import timber.log.Timber


@JvmField
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Update Notifications "

const val CHANNEL_ID = "DOWNLOAD_UPDATES"
const val NOTIFICATION_ID = 1

const val NOTIFICATION_CONDITION = "NOTIFY"
const val CHANNEL_LIST_NAME = "CHANNEL_LIST_NAME"
const val DOWNLOAD_PROGRAMS = "DOWNLOAD_PROGRAMS"
const val CHANNEL_INDEX = "CHANNEL_INDEX"
const val CHANNEL_COUNT = "CHANNEL_COUNT"
const val CHANNEL_MISSING_COUNT = "CHANNEL_MISSING_COUNT"

/**
 * Create a Notification that is shown as a heads-up notification if possible.
 *
 * @param message Message shown on the notification
 * @param context Context needed to create Toast
 */

fun makeStatusNotification(message: String, context: Context) {

    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        // .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun hideStatusNotification(context: Context) {
    // Indicate whether the work finished successfully with the Result
    NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
}

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

fun createInputDataForUri(listName: String, missingArray: Array<String>, isNotificationOn: Boolean): Data {
    return Data.Builder().apply {
        putString(CHANNEL_LIST_NAME, listName)
        putStringArray(CHANNEL_MISSING_COUNT, missingArray)
        putBoolean(NOTIFICATION_CONDITION, isNotificationOn)
    }.build()
}
