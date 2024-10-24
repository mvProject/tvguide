package com.mvproject.tvprogramguide.domain.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mvproject.tvprogramguide.R
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Helper class to interact with the Notification layer.
 * Provides methods to show and hide different types of notifications.
 *
 * @property context The application context, injected using Hilt.
 */
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
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

            val myAudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID,
                    PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    setSound(soundUri, myAudioAttributes)
                }
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

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
    fun hideScheduledProgramNotification(id: Int) {
        try {
            NotificationManagerCompat.from(context).cancel(id)
        } catch (ex: SecurityException) {
            Timber.e("security exception ${ex.message}")
        } catch (ex: Exception) {
            Timber.e("exception ${ex.message}")
        }
    }

    /**
     * Creates and shows a status notification, displayed as a heads-up notification if possible.
     * This is typically used for update-related notifications.
     *
     * @param message The message to be displayed in the notification.
     */
    fun makeStatusNotification(message: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = UPDATE_NOTIFICATION_CHANNEL_NAME
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(UPDATE_NOTIFICATION_CHANNEL_ID, name, importance)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

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
    }

    /**
     * Dismisses the current status notification.
     * This is typically used to hide the update-related notification.
     */
    fun hideStatusNotification() {
        NotificationManagerCompat.from(context).cancel(UPDATE_NOTIFICATION_ID)
    }

    private companion object {
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME = "Schedule Notifications "
        const val PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID = "Program Schedule"

        const val UPDATE_NOTIFICATION_CHANNEL_NAME = "Update Notifications "
        const val UPDATE_NOTIFICATION_CHANNEL_ID = "Download Updates"
        const val UPDATE_NOTIFICATION_ID = 1001
    }
}
