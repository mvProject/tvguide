package com.mvproject.tvprogramguide.data.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import timber.log.Timber
import java.util.*

/**
 * Gets the [AlarmManager] system service.
 *
 * @return the [AlarmManager] system service
 */
fun Context.getAlarmManager() =
    getSystemService(Context.ALARM_SERVICE) as? AlarmManager

/**
 * Sets a alarm using [AlarmManagerCompat] to be triggered based on the given parameter.
 *
 * **This function can only be called if the permission `SCHEDULE_EXACT_ALARM` is granted to
 * the application.**
 *
 * @see [android.Manifest.permission.SCHEDULE_EXACT_ALARM]
 *
 * @param triggerAtMillis time in milliseconds that the alarm should go off, using the
 * appropriate clock (depending on the alarm type).
 * @param operation action to perform when the alarm goes off
 * @param type type to define how the alarm will behave
 */
fun Context.setExactAlarm(
    triggerAtMillis: Long,
    operation: PendingIntent?,
    type: Int = AlarmManager.RTC_WAKEUP
) {
    val currentTime = Calendar.getInstance().timeInMillis
    if (triggerAtMillis <= currentTime) {
        Timber.w("testing It is not possible to set alarm in the past")
        return
    }

    if (operation == null) {
        Timber.e("testing PendingIntent is null")
        return
    }

    getAlarmManager()?.let { manager ->
        val isSucceed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            manager.canScheduleExactAlarms()
        } else {
            true
        }
        if (isSucceed) {
            Timber.d("testing setExactAlarm to $triggerAtMillis, (${triggerAtMillis.convertTimeToReadableFormat()})")
            AlarmManagerCompat
                .setExactAndAllowWhileIdle(manager, type, triggerAtMillis, operation)
        } else {
            Timber.e("setExactAlarm is not succeed")
        }
    }
}

/**
 * Cancels a alarm set on [AlarmManager], based on the given [PendingIntent].
 *
 * @param operation action to be canceled
 */
fun Context.cancelAlarm(operation: PendingIntent?) {
    if (operation == null) {
        Timber.e("PendingIntent is null")
        return
    }

    getAlarmManager()?.let { manager ->
        manager.cancel(operation)
    }
}