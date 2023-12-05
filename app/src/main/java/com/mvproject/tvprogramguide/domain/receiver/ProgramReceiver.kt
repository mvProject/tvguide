package com.mvproject.tvprogramguide.domain.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class ProgramReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ALARM_ACTION -> intent.let { data ->
                val channel = data.getStringExtra(EXTRA_CHANNEL) ?: EXTRA_CHANNEL
                val program = data.getStringExtra(EXTRA_PROGRAM) ?: EXTRA_PROGRAM
                val id = data.getIntExtra(EXTRA_ID, COUNT_ZERO)
                notificationHelper.showScheduledProgramNotification(
                    id,
                    program,
                    channel
                )
            }
            Intent.ACTION_BOOT_COMPLETED,
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
            }
            else ->
                Timber.e("Action not supported")
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_PROGRAM = "extra_program"
        const val EXTRA_CHANNEL = "extra_channel"
        const val ALARM_ACTION = "com.mvproject.tvprogramguide.SET_ALARM"
    }
}
