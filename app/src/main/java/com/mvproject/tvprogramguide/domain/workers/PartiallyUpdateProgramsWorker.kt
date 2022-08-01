package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.domain.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class PartiallyUpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val channelProgramRepository: ChannelProgramRepository
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
                Timber.e("testing PartiallyUpdateProgramsWorker update count zero")
            }
        }

        if (isNotificationOn) {
            hideStatusNotification(context = applicationContext)
        }

        return Result.success()
    }
}
