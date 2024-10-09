package com.mvproject.tvprogramguide.ui.screens.usercustomlist.action

import com.mvproject.tvprogramguide.data.model.domain.ChannelList

sealed class ChannelListAction {
    data class AddList(val listName: String) : ChannelListAction()
    data class DeleteList(val list: ChannelList) : ChannelListAction()
}
