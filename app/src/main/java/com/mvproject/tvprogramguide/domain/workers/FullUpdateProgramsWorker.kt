package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.domain.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.domain.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.utils.*
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class FullUpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val storeHelper: StoreHelper
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            makeStatusNotification(
                applicationContext.getString(R.string.notif_programs_download),
                applicationContext
            )
        }

        val selectedChannels = selectedChannelRepository.loadSelectedChannelsIds()
        val channelsCount = selectedChannels.count()
        if (channelsCount > COUNT_ZERO) {
            selectedChannels.forEachIndexed { ind, chn ->
                channelProgramRepository.loadProgram(chn)
                setProgressAsync(
                    Data.Builder()
                        .putInt(CHANNEL_INDEX, ind)
                        .putInt(CHANNEL_COUNT, channelsCount)
                        .build()
                )
            }
            storeHelper.setProgramsUpdateLastTime(System.currentTimeMillis())
        } else {
            Timber.e("testing FullUpdateProgramsWorker update count zero")
        }

        if (isNotificationOn) {
            hideStatusNotification(applicationContext)
        }

        return Result.success()
    }

}