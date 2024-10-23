package com.mvproject.tvprogramguide.data.model.domain

import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.empty

data class SelectionChannel(
    val channelId: String,
    val programId: String = String.empty,
    val channelName: String = String.empty,
    val channelIcon: String = String.empty,
    val order: Int = COUNT_ZERO,
    val parentList: String = String.empty,
    val isSelected: Boolean = false,
)
