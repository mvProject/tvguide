package com.mvproject.tvprogramguide.customlists

import com.mvproject.tvprogramguide.model.data.Channel

sealed class AvailableChannelsAction {
    data class ChannelAdd(val channel: Channel) : AvailableChannelsAction()
    data class ChannelDelete(val channel: Channel) : AvailableChannelsAction()
    data class ChannelFilter(val query: String) : AvailableChannelsAction()
}