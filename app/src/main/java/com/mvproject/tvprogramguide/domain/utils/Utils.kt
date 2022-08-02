package com.mvproject.tvprogramguide.domain.utils

import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

object Utils {

    val actualDay
        get() = Clock.System.now()
            .minus(1.days)
            .toEpochMilliseconds()

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
}
