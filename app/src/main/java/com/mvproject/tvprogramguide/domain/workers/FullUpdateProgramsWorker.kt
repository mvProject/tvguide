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
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.utils.AppConstants
import com.mvproject.tvprogramguide.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.utils.NOTIFICATION_CONDITION
import com.mvproject.tvprogramguide.utils.TimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class FullUpdateProgramsWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        private val selectedChannelRepository: SelectedChannelRepository,
        private val channelProgramRepository: ChannelProgramRepository,
        private val preferenceRepository: PreferenceRepository,
        private val notificationHelper: NotificationHelper,
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            val applicationContext = applicationContext

            val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
            if (isNotificationOn) {
                notificationHelper.makeStatusNotification(
                    message = applicationContext.getString(R.string.notification_programs_download),
                )
            }

            val selectedChannels = selectedChannelRepository.loadSelectedChannelsIds()

            val channelsCount = selectedChannels.count()
            if (channelsCount > AppConstants.COUNT_ZERO) {
                selectedChannels.forEachIndexed { ind, chn ->
                    channelProgramRepository.loadProgram(channelId = chn)
                    val channelNumber = ind + AppConstants.COUNT_ONE
                    setProgressAsync(
                        Data
                            .Builder()
                            .putInt(CHANNEL_INDEX, channelNumber)
                            .putInt(CHANNEL_COUNT, channelsCount)
                            .build(),
                    )
                }
                preferenceRepository.apply {
                    setProgramsUpdateLastTime(timeInMillis = TimeUtils.actualDate)
                    setChannelsCountChanged(false)
                    setChannelsUpdateRequired(false)
                }
            } else {
                Timber.e("FullUpdateProgramsWorker update count zero")
            }

            if (isNotificationOn) {
                notificationHelper.hideStatusNotification()
            }

            return Result.success()
        }
    }
