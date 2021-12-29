package com.mvproject.tvprogramguide.model.data

data class Channel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    var isSelected: Boolean = false
) : IChannel()