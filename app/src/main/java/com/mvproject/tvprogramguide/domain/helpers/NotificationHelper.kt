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
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun showScheduledProgramNotification(id: Int, programTitle: String, channelTitle: String) {

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val myAudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
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

        // Create the notification
        val builder = NotificationCompat.Builder(
            context,
            PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle(channelTitle)
            .setContentText(programTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Show the notification
        NotificationManagerCompat.from(context).notify(id, builder.build())
    }

    fun hideScheduledProgramNotification(id: Int) {
        // Indicate whether the work finished successfully with the Result
        NotificationManagerCompat.from(context).cancel(id)
    }

    /**
     * Create a Notification that is shown as a heads-up notification if possible.
     *
     * @param message Message shown on the notification
     */

    fun makeStatusNotification(message: String) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = UPDATE_NOTIFICATION_CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(UPDATE_NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, UPDATE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(UPDATE_NOTIFICATION_ID, builder.build())
    }

    fun hideStatusNotification() {
        // Indicate whether the work finished successfully with the Result
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