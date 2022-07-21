package com.mvproject.tvprogramguide.ui.settings.channels.actions

import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel

sealed class AvailableChannelsAction {
    data class ChannelAdd(val selectedChannel: AvailableChannel) : AvailableChannelsAction()
    data class ChannelDelete(val selectedChannel: AvailableChannel) : AvailableChannelsAction()
    data class ChannelFilter(val query: String) : AvailableChannelsAction()
}
