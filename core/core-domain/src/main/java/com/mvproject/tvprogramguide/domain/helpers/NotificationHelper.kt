package com.mvproject.tvprogramguide.domain.helpers

import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mvproject.tvprogramguide.core.domain.R
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
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(
                context,
                PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(channelTitle)
                .setContentText(programTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setSound(soundUri)

            NotificationManagerCompat.from(context).notify(id, builder.build())
        } catch (ex: SecurityException) {
            Timber.e("security exception ${ex.message}")
        } catch (ex: Exception) {
            Timber.e("exception ${ex.message}")
        }
    }

    companion object {
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME = "Schedule Notifications "
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID = "Program Schedule"
    }
}
