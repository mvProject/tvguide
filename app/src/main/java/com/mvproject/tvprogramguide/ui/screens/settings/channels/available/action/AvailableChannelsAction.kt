package com.mvproject.tvprogramguide.ui.screens.settings.channels.available.action

import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel

sealed class AvailableChannelsAction {
    data class ChannelAdd(val selectedChannel: AvailableChannel) : AvailableChannelsAction()
    data class ChannelDelete(val selectedChannel: AvailableChannel) : AvailableChannelsAction()
    data class ChannelFilter(val query: String) : AvailableChannelsAction()
}
