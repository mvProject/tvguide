package com.mvproject.tvprogramguide.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import timber.log.Timber
import java.util.Calendar

/**
 * Extension function to get the AlarmManager system service.
 *
 * @return The AlarmManager system service, or null if it's not available.
 */
fun Context.getAlarmManager() = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

/**
 * Extension function to set an exact alarm using AlarmManagerCompat.
 * This alarm will be triggered at the specified time, even if the device is in low-power idle modes.
 *
 * Note: This function requires the SCHEDULE_EXACT_ALARM permission for Android 12 (API level 31) and above.
 *
 * @param triggerAtMillis The time in milliseconds when the alarm should be triggered.
 * @param operation The PendingIntent to be executed when the alarm is triggered.
 * @param type The type of alarm to set (default is AlarmManager.RTC_WAKEUP).
 */
fun Context.setExactAlarm(
    triggerAtMillis: Long,
    operation: PendingIntent?,
    type: Int = AlarmManager.RTC_WAKEUP,
) {
    val currentTime = Calendar.getInstance().timeInMillis
    if (triggerAtMillis <= currentTime) {
        Timber.w("It is not possible to set alarm in the past")
        return
    }

    if (operation == null) {
        Timber.e("PendingIntent is null")
        return
    }

    getAlarmManager()?.let { manager ->
        val isSucceed =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                manager.canScheduleExactAlarms()
            } else {
                true
            }
        if (isSucceed) {
            Timber.d("testing setExactAlarm to $triggerAtMillis, (${triggerAtMillis.convertTimeToReadableFormat()})")
            AlarmManagerCompat
                .setExactAndAllowWhileIdle(manager, type, triggerAtMillis, operation)
        } else {
            Timber.e("testing setExactAlarm is not succeed")
        }
    }
}

/**
 * Extension function to cancel a previously set alarm.
 *
 * @param operation The PendingIntent associated with the alarm to be canceled.
 */
fun Context.cancelAlarm(operation: PendingIntent?) {
    if (operation == null) {
        Timber.e("PendingIntent is null")
        return
    }

    getAlarmManager()?.cancel(operation)
}
