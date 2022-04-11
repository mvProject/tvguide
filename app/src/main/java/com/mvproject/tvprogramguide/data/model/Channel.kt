package com.mvproject.tvprogramguide.data.model

data class Channel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    var isSelected: Boolean = false
)
