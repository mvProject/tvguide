package com.mvproject.tvprogramguide.ui.screens.channels.selected.actions

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program

sealed class ChannelsViewAction {
    data object CompleteOnBoard : ChannelsViewAction()
    data object ReloadChannels : ChannelsViewAction()
    data object RefreshChannels : ChannelsViewAction()
    data class SelectChannelList(val list: ChannelList) : ChannelsViewAction()
    data class ToggleScheduleProgram(val channelName:String,val program: Program) : ChannelsViewAction()
}