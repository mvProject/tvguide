package com.mvproject.tvprogramguide.ui.usercustomlist.action

import com.mvproject.tvprogramguide.data.model.CustomList

sealed class UserListAction {
    data class AddList(val listName: String) : UserListAction()
    data class DeleteList(val list: CustomList) : UserListAction()
}