package com.mvproject.tvprogramguide.domain.helpers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mvproject.tvprogramguide.R
import timber.log.Timber

/**
 * Helper class to interact with the Notification layer.
 * Provides methods to show and hide different types of notifications.
 *
 * @property context The application context, injected using Hilt.
 */
class NotificationHelper(private val context: Context) {
    /**
     * Shows a notification for a scheduled TV program.
     *
     * @param id The unique identifier for the notification.
     * @param programTitle The title of the TV program to be displayed in the notification content.
     * @param channelTitle The title of the TV channel to be displayed in the notification title.
     */
    fun showScheduledProgramNotification(id: Int, programTitle: String, channelTitle: String) {
        try {
            val builder = NotificationCompat.Builder(
                context,
                PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(channelTitle)
                .setContentText(programTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            NotificationManagerCompat.from(context).notify(id, builder.build())
        } catch (ex: SecurityException) {
            Timber.e("security exception ${ex.message}")
        } catch (ex: Exception) {
            Timber.e("exception ${ex.message}")
        }
    }

    /**
     * Dismisses a specific notification for a scheduled TV program.
     *
     * @param id The unique identifier of the notification to be dismissed.
     */
/*    fun hideScheduledProgramNotification(id: Int) {
        try {
            NotificationManagerCompat.from(context).cancel(id)
        } catch (ex: SecurityException) {
            Timber.e("security exception ${ex.message}")
        } catch (ex: Exception) {
            Timber.e("exception ${ex.message}")
        }
    }*/

    /**
     * Creates and shows a status notification, displayed as a heads-up notification if possible.
     * This is typically used for update-related notifications.
     *
     * @param message The message to be displayed in the notification.
     */
/*    fun makeStatusNotification(message: String) {
        try {
            val builder = NotificationCompat.Builder(context, UPDATE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(LongArray(0))

            NotificationManagerCompat.from(context).notify(UPDATE_NOTIFICATION_ID, builder.build())
        } catch (ex: SecurityException) {
            Timber.e("security exception ${ex.message}")
        } catch (ex: Exception) {
            Timber.e("exception ${ex.message}")
        }
    }*/

    /**
     * Dismisses the current status notification.
     * This is typically used to hide the update-related notification.
     */
/*    fun hideStatusNotification() {
        NotificationManagerCompat.from(context).cancel(UPDATE_NOTIFICATION_ID)
    }*/

     companion object {
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME = "Schedule Notifications "
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID = "Program Schedule"
    }
}
