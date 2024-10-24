package com.mvproject.tvprogramguide.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.usecases.UpdateProgramsUseCase
import com.mvproject.tvprogramguide.utils.NOTIFICATION_CONDITION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

/**
 * Worker class responsible for performing a full update of TV programs.
 * This worker is triggered to update program information for all channels or a specific set of channels.
 */
@HiltWorker
class FullUpdateProgramsWorker
@AssistedInject
constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    //  private val preferenceRepository: PreferenceRepository,
    private val notificationHelper: NotificationHelper,
    private val updateProgramsUseCase: UpdateProgramsUseCase,
) : CoroutineWorker(context, params) {
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
    override suspend fun doWork(): Result {
        val applicationContext = applicationContext
        // Show notification if required
        val isNotificationOn = inputData.getBoolean(NOTIFICATION_CONDITION, false)
        if (isNotificationOn) {
            notificationHelper.makeStatusNotification(
                message = applicationContext.getString(R.string.notification_programs_download),
            )
        }
        Timber.d("testing FullUpdateProgramsWorker start update")
        updateProgramsUseCase(channelId = "channelId")
        /*      preferenceRepository.apply {
                  setProgramsUpdateLastTime(timeInMillis = TimeUtils.actualDate)
               //   setChannelsForUpdate(emptyList())
                  setProgramsUpdateRequiredState(false)
              }*/
        Timber.w("testing FullUpdateProgramsWorker end update")

        /*     val selectedChannels = preferenceRepository.getChannelsForUpdate().first()
             Timber.w("testing FullUpdateProgramsWorker selectedChannels $selectedChannels")

             val channelsCount = selectedChannels.count()
             if (channelsCount > COUNT_ZERO) {
                 selectedChannels.forEachIndexed { ind, channelId ->
                     delay(WORKER_DELAY)

                     updateProgramsUseCase(channelId = channelId)

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
                     setChannelsForUpdate(emptyList())
                     setChannelsUpdateRequiredState(false)
                 }
             } else {
                 Timber.e("FullUpdateProgramsWorker update count zero")
             }*/
        // Hide notification after update
        if (isNotificationOn) {
            notificationHelper.hideStatusNotification()
        }

        return Result.success()
    }
}
