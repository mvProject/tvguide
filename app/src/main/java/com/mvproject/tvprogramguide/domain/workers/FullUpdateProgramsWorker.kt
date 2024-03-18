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

@HiltWorker
class FullUpdateProgramsWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        private val updateProgramsUseCase: UpdateProgramsUseCase,
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

            updateProgramsUseCase()

            if (isNotificationOn) {
                notificationHelper.hideStatusNotification()
            }

            return Result.success()
        }
    }
