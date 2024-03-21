package com.mvproject.tvprogramguide.data.model.domain

import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

data class SelectionChannel(
    val channelId: String,
    val channelName: String = NO_VALUE_STRING,
    val channelIcon: String = NO_VALUE_STRING,
    val order: Int = COUNT_ZERO,
    val parentList: String = NO_VALUE_STRING,
    val isSelected: Boolean = false,
)
