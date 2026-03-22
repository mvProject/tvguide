package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Immutable
data class Program(
    val programId: String,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
    val category: String = String.empty,
    val channel: String = String.empty,
    val scheduledId: Long? = null,
) {
    val programProgress: Float
        get() {
            val currTime = System.currentTimeMillis()
            if (currTime <= dateTimeStart) return 0f
            val endValue = (dateTimeEnd - dateTimeStart).toInt()
            val spendValue = (currTime - dateTimeStart).toDouble()
            return (spendValue / endValue).toFloat()
        }
}
