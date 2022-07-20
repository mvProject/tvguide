package com.mvproject.tvprogramguide.domain.utils

import android.content.Context
import android.content.ContextWrapper
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity

object Utils {

    val actualDay = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS

    fun Context.pxToDp(px: Float): Int {
        return (px.toInt() / resources.displayMetrics.density).toInt()
    }

    fun calculateProgramProgress(startTime: Long, endTime: Long): Float {
        var progressValue = 0f
        val currTime = System.currentTimeMillis()
        if (currTime > startTime) {
            val endValue = (endTime - startTime).toInt()
            val spendValue = (currTime - startTime).toDouble()
            progressValue = (spendValue / endValue).toFloat()
        }
        return progressValue
    }

    fun Context.findActivity(): AppCompatActivity? = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }


}
