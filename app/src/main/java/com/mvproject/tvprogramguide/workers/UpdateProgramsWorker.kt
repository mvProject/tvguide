package com.mvproject.tvprogramguide.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, true)
        val str = inputData.getString(CHANNEL_LIST_NAME)

        val ll = str?.let { selectedChannelRepository.loadSelectedChannels(it) }
        ll?.forEachIndexed { ind, chn ->
            channelProgramRepository.loadProgram(chn.channelId)
            setProgressAsync(
                Data.Builder()
                    .putInt(CHANNEL_INDEX, ind)
                    .build()
            )
            sleep()
        }

        if (isNotificationOn) {
            makeStatusNotification(
                applicationContext.getString(R.string.notif_programs_download),
                applicationContext
            )
        }

        if (isNotificationOn) {
            hideStatusNotification(applicationContext)
        }

        return Result.success()
    }

}