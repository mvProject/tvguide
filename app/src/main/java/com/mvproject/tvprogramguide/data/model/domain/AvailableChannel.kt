package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class AvailableChannel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    var isSelected: Boolean = false,
)
