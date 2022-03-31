package com.mvproject.tvprogramguide.ui.settings.channels

import com.mvproject.tvprogramguide.data.model.Channel

sealed class SelectedChannelsAction {
    data class ChannelDelete(val channel: Channel) : SelectedChannelsAction()
}