package com.mvproject.tvprogramguide.ui.screens.usercustomlist.action

import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList

sealed class UserListAction {
    data class AddList(val listName: String) : UserListAction()
    data class DeleteList(val list: UserChannelsList) : UserListAction()
}
