package com.mvproject.tvprogramguide.domain.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.utils.cancelAlarm
import com.mvproject.tvprogramguide.data.utils.setExactAlarm
import com.mvproject.tvprogramguide.domain.receiver.ProgramReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper to interact with the Alarm layer.
 * @property context application context
 */

@Singleton
class ProgramSchedulerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Schedules a program alarm based on the due date.
     *
     * @param programSchedule program to be scheduled
     */
    fun scheduleProgramAlarm(programSchedule: ProgramSchedule) {
        val receiverIntent = Intent(context, ProgramReceiver::class.java).apply {
            action = ProgramReceiver.ALARM_ACTION
            putExtra(ProgramReceiver.EXTRA_ID, programSchedule.scheduleId.toInt())
            putExtra(ProgramReceiver.EXTRA_PROGRAM, programSchedule.programTitle)
            putExtra(ProgramReceiver.EXTRA_CHANNEL, programSchedule.channelTitle)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            programSchedule.scheduleId.toInt(),
            receiverIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        context.setExactAlarm(programSchedule.triggerTime, pendingIntent)
    }

    /**
     * Cancels a program alarm based on the program scheduler id.
     *
     * @param schedulerId program scheduler id to be canceled
     */
    fun cancelProgramAlarm(schedulerId: Long) {
        val receiverIntent = Intent(context, ProgramReceiver::class.java)
        receiverIntent.action = ProgramReceiver.ALARM_ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedulerId.toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        context.cancelAlarm(pendingIntent)
    }
}
