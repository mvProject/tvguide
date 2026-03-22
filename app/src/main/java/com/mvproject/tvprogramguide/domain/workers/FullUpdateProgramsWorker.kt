package com.mvproject.tvprogramguide.domain.workers

import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.domain.usecases.UpdateProgramsUseCase
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.TOTAL_CHANNELS_COUNT
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Worker class responsible for performing a full update of TV programs.
 * This worker is triggered to update program information for all channels or a specific set of channels.
 */
class FullUpdateProgramsWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val updateProgramsUseCase: UpdateProgramsUseCase by inject()

    /**
     * Performs the work of updating TV programs.
     *
     * This function:
     * 1. Shows a notification if required
     * 2. Calls the updateProgramsUseCase to update program information
     * 3. Hides the notification after the update is complete
     *
     * @return Result indicating the outcome of the work (success in this case)
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun doWork(): Result {

        Timber.d("FullUpdateProgramsWorker start update")

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val updateNotificationBuilder = NotificationCompat.Builder(
            applicationContext,
            UPDATE_NOTIFICATION_CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.ic_notify)
            setContentText(applicationContext.getString(R.string.notification_programs_download))
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setVibrate(LongArray(0))
            setOnlyAlertOnce(true)
        }

        val channelsCount = TOTAL_CHANNELS_COUNT
        var current = COUNT_ZERO

        setForeground(
            ForegroundInfo(
                UPDATE_NOTIFICATION_ID,
                updateNotificationBuilder.build(),
                FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        )
        updateNotificationBuilder.apply {
            setProgress(channelsCount, current, false)
            setContentText("Updating: $current%")
        }

        if (runAttemptCount >= MAX_RUN_ATTEMPTS) {
            Timber.e("FullUpdateProgramsWorker giving up after $runAttemptCount attempts")
            return Result.failure()
        }

        return try {
            updateProgramsUseCase {
                current += COUNT_ONE
                // Throttle notification updates to avoid flooding the notification system
                if (current % NOTIFICATION_UPDATE_INTERVAL == 0) {
                    val progress = ((current / channelsCount.toFloat()) * 100).toInt()

                    updateNotificationBuilder.apply {
                        setProgress(channelsCount, current, false)
                        setContentText("Updating: $progress%")
                    }

                    notificationManager.notify(
                        UPDATE_NOTIFICATION_ID,
                        updateNotificationBuilder.build()
                    )
                }
            }

            Timber.d("FullUpdateProgramsWorker end update")
            Result.success()
        } catch (ex: Exception) {
            Timber.e("FullUpdateProgramsWorker failed (attempt ${runAttemptCount + 1}/$MAX_RUN_ATTEMPTS): ${ex.message}")
            Result.retry()
        }
    }

    companion object {
        const val UPDATE_NOTIFICATION_CHANNEL_ID = "Download Updates"
        const val UPDATE_NOTIFICATION_CHANNEL_NAME = "Update Notifications"
        const val UPDATE_NOTIFICATION_ID = 1001
        private const val MAX_RUN_ATTEMPTS = 3
        private const val NOTIFICATION_UPDATE_INTERVAL = 5
    }
}


