package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.domain.utils.NOTIFICATION_CONDITION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.Clock
import timber.log.Timber

@HiltWorker
class FullUpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val preferenceRepository: PreferenceRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            notificationHelper.makeStatusNotification(
                message = applicationContext.getString(R.string.notification_programs_download)
            )
        }

        val selectedChannels = selectedChannelRepository.loadSelectedChannelsIds()

        val channelsCount = selectedChannels.count()
        if (channelsCount > COUNT_ZERO) {
            selectedChannels.forEachIndexed { ind, chn ->
                channelProgramRepository.loadProgram(channelId = chn)
                val channelNumber = ind + 1
                setProgressAsync(
                    Data.Builder()
                        .putInt(CHANNEL_INDEX, channelNumber)
                        .putInt(CHANNEL_COUNT, channelsCount)
                        .build()
                )
            }
            preferenceRepository.setProgramsUpdateLastTime(
                timeInMillis = Clock.System.now().toEpochMilliseconds()
            )
        } else {
            Timber.e("FullUpdateProgramsWorker update count zero")
        }

        if (isNotificationOn) {
            notificationHelper.hideStatusNotification()
        }

        return Result.success()
    }
}
