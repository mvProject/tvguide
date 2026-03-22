package com.mvproject.tvprogramguide.ui.screens.settings.channels.state

import com.mvproject.tvprogramguide.utils.AppConstants.empty

data class ChannelSettingsState(
    val isComplete: Boolean = false,
    val searchString: String = String.empty,
)
