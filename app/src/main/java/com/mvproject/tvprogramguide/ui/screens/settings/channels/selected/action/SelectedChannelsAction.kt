package com.mvproject.tvprogramguide.ui.screens.settings.channels.selected.action

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel

sealed class SelectedChannelsAction {
    data class ChannelDelete(val selectedChannel: SelectedChannel) : SelectedChannelsAction()
    data class ChannelsReorder(val selectedChannels: List<SelectedChannel>) :
        SelectedChannelsAction()
}
