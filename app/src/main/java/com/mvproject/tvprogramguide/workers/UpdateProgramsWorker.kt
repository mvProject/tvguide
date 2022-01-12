package com.mvproject.tvprogramguide.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class UpdateProgramsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val storeHelper: StoreHelper
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext

        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, true)
        if (isNotificationOn) {
            makeStatusNotification(
                applicationContext.getString(R.string.notif_programs_download),
                applicationContext
            )
        }
        val channelListName = inputData.getString(CHANNEL_LIST_NAME)

        channelListName?.let { listname ->
            val updateIds = inputData.getStringArray(CHANNEL_MISSING_COUNT)
            val idsCount = updateIds?.count() ?: COUNT_ZERO
            Timber.d("testing UpdateProgramsWorker partially update idsCount $idsCount")
            if (idsCount > COUNT_ZERO) {
                updateIds?.forEachIndexed { ind, id ->
                    channelProgramRepository.loadProgram(id)
                    setProgressAsync(
                        Data.Builder()
                            .putInt(CHANNEL_INDEX, ind)
                            .putInt(CHANNEL_COUNT, idsCount)
                            .build()
                    )
                }
            } else {
                val selectedChannels = selectedChannelRepository.loadSelectedChannels(listname)
                val channelsCount = selectedChannels.count()
                Timber.d("testing UpdateProgramsWorker full update")
                if (channelsCount > COUNT_ZERO) {
                    selectedChannels.forEachIndexed { ind, chn ->
                        channelProgramRepository.loadProgram(chn.channelId)
                        setProgressAsync(
                            Data.Builder()
                                .putInt(CHANNEL_INDEX, ind)
                                .putInt(CHANNEL_COUNT, channelsCount)
                                .build()
                        )
                    }
                    storeHelper.setProgramsUpdateLastTime(System.currentTimeMillis())
                } else {
                    Timber.d("testing UpdateProgramsWorker full update count zero")
                }
            }
        }

        if (isNotificationOn) {
            hideStatusNotification(applicationContext)
        }

        return Result.success()
    }

}