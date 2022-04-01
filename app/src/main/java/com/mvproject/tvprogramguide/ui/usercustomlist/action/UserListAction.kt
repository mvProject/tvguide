package com.mvproject.tvprogramguide.ui.usercustomlist.action

import com.mvproject.tvprogramguide.data.entity.CustomListEntity

sealed class UserListAction {
    data class AddList(val listName: String) : UserListAction()
    data class DeleteList(val list: CustomListEntity) : UserListAction()
}