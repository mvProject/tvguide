package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.utils.NOTIFICATION_CONDITION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.Clock

@HiltWorker
class UpdateChannelsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val allChannelRepository: AllChannelRepository,
    private val preferenceRepository: PreferenceRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            notificationHelper.makeStatusNotification(
                message = applicationContext.getString(R.string.notification_channels_download)
            )
        }

        allChannelRepository.loadProgramFromSource()
        preferenceRepository.setChannelsUpdateLastTime(
            timeInMillis = Clock.System.now().toEpochMilliseconds()
        )

        if (isNotificationOn) {
            notificationHelper.hideStatusNotification()
        }

        return Result.success()
    }
}
