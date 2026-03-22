package com.mvproject.tvprogramguide.ui.screens.settings.channels.action

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel

sealed class ChannelsAction {
    data class ToggleSelection(val channel: SelectionChannel) : ChannelsAction()

    data class DeleteSelection(val selectedChannel: SelectionChannel) : ChannelsAction()

    data class ChannelFilter(val query: String) : ChannelsAction()

    data class ChannelsReorder(val selectedChannels: List<SelectionChannel>) :
        ChannelsAction()
}
