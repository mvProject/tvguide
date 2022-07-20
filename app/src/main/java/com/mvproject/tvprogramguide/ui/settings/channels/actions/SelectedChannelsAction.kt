package com.mvproject.tvprogramguide.ui.settings.channels.actions

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel

sealed class SelectedChannelsAction {
    data class ChannelDelete(val selectedChannel: SelectedChannel) : SelectedChannelsAction()
}
