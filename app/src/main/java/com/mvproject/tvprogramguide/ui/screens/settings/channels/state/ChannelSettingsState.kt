package com.mvproject.tvprogramguide.ui.screens.settings.channels.state

import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

data class ChannelSettingsState(
    val isComplete: Boolean = false,
    val searchString: String = NO_VALUE_STRING,
)
