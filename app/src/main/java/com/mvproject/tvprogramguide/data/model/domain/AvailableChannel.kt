package com.mvproject.tvprogramguide.data.model.domain

data class AvailableChannel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    val isSelected: Boolean = false,
)
