package com.mvproject.tvprogramguide.ui.screens.channellist.action

import com.mvproject.tvprogramguide.data.model.domain.ChannelList

sealed class ChannelListAction {
    data class AddList(val listName: String) : ChannelListAction()
    data class DeleteList(val list: ChannelList) : ChannelListAction()
    data class SelectList(val list: ChannelList) : ChannelListAction()
}
