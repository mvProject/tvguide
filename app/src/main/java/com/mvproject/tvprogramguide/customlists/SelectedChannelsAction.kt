package com.mvproject.tvprogramguide.customlists

import com.mvproject.tvprogramguide.model.data.Channel

sealed class SelectedChannelsAction {
    data class ChannelDelete(val channel: Channel) : SelectedChannelsAction()
}