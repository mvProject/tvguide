package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class ChannelList(
    val id: Int,
    val listName: String,
    val isSelected: Boolean
)
