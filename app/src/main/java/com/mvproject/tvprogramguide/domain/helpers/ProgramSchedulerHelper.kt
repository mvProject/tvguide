package com.mvproject.tvprogramguide.domain.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.domain.receiver.ProgramReceiver
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.cancelAlarm
import com.mvproject.tvprogramguide.utils.setExactAlarm
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper to interact with the Alarm layer.
 * @property context application context
 */

@Singleton
class ProgramSchedulerHelper
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        /**
         * Schedules a program alarm based on the due date.
         *
         * @param programSchedule program to be scheduled
         * @param channelName program to be scheduled
         */

        fun scheduleProgramAlarm(
            programSchedule: Program,
            channelName: String,
        ) {
            val receiverIntent =
                Intent(context, ProgramReceiver::class.java).apply {
                    action = ProgramReceiver.ALARM_ACTION
                    putExtra(
                        ProgramReceiver.EXTRA_ID,
                        programSchedule.scheduledId?.toInt() ?: COUNT_ZERO,
                    )
                    putExtra(ProgramReceiver.EXTRA_PROGRAM, programSchedule.title)
                    putExtra(ProgramReceiver.EXTRA_CHANNEL, channelName)
                }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    programSchedule.scheduledId?.toInt() ?: COUNT_ZERO,
                    receiverIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

            context.setExactAlarm(programSchedule.dateTimeStart, pendingIntent)
        }

        /**
         * Cancels a program alarm based on the program scheduler id.
         *
         * @param schedulerId program scheduler id to be canceled
         */
        fun cancelProgramAlarm(schedulerId: Long) {
            val receiverIntent = Intent(context, ProgramReceiver::class.java)
            receiverIntent.action = ProgramReceiver.ALARM_ACTION

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    schedulerId.toInt(),
                    receiverIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

            context.cancelAlarm(pendingIntent)
        }
    }
