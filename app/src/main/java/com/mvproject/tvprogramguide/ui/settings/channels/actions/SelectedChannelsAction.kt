package com.mvproject.tvprogramguide.ui.settings.channels.actions

import com.mvproject.tvprogramguide.data.model.Channel

sealed class SelectedChannelsAction {
    data class ChannelDelete(val channel: Channel) : SelectedChannelsAction()
}
