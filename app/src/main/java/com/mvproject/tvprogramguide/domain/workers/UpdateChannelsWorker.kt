package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.utils.NOTIFICATION_CONDITION
import com.mvproject.tvprogramguide.domain.utils.hideStatusNotification
import com.mvproject.tvprogramguide.domain.utils.makeStatusNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateChannelsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val allChannelRepository: AllChannelRepository,
    private val preferenceRepository: PreferenceRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            makeStatusNotification(
                message = applicationContext.getString(R.string.notif_programs_download),
                context = applicationContext
            )
        }

        allChannelRepository.loadProgramFromSource()
        preferenceRepository.setChannelsUpdateLastTime(timeInMillis = System.currentTimeMillis())

        if (isNotificationOn) {
            hideStatusNotification(context = applicationContext)
        }

        return Result.success()
    }
}
