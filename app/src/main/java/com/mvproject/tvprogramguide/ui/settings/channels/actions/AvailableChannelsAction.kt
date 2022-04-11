package com.mvproject.tvprogramguide.ui.settings.channels.actions

import com.mvproject.tvprogramguide.data.model.Channel

sealed class AvailableChannelsAction {
    data class ChannelAdd(val channel: Channel) : AvailableChannelsAction()
    data class ChannelDelete(val channel: Channel) : AvailableChannelsAction()
    data class ChannelFilter(val query: String) : AvailableChannelsAction()
}
