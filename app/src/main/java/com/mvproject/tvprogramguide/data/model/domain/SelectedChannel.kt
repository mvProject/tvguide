package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedChannel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    val order: Int = 0,
    val parentList: String = ""
)