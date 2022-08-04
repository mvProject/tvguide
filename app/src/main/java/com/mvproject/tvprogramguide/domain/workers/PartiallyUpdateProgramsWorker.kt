package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_MISSING_COUNT
import com.mvproject.tvprogramguide.domain.utils.NOTIFICATION_CONDITION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class PartiallyUpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val channelProgramRepository: ChannelProgramRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            notificationHelper.makeStatusNotification(
                message = applicationContext.getString(R.string.notif_programs_download)
            )
        }
        val updateIds = inputData.getStringArray(CHANNEL_MISSING_COUNT)

        updateIds?.let { ids ->
            val idsCount = ids.count()
            if (idsCount > COUNT_ZERO) {
                ids.forEachIndexed { ind, id ->
                    channelProgramRepository.loadProgram(id)
                    setProgressAsync(
                        Data.Builder()
                            .putInt(CHANNEL_INDEX, ind)
                            .putInt(CHANNEL_COUNT, idsCount)
                            .build()
                    )
                }
            } else {
                Timber.e("PartiallyUpdateProgramsWorker update count zero")
            }
        }

        if (isNotificationOn) {
            notificationHelper.hideStatusNotification()
        }

        return Result.success()
    }
}
