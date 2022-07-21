package com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCustomListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val storeHelper: StoreHelper
) : ViewModel() {

    private var _customs = MutableStateFlow<List<UserChannelsList>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch {
            customListRepository.loadChannelsLists().collect { lists ->
                _customs.emit(lists)
            }
        }
    }

    private fun checkForDefaultAfterAdd() {
        if (customs.value.count() == COUNT_ONE) {
            val listName = customs.value.first().listName
            storeHelper.setDefaultChannelList(name = listName)
        }
    }

    private fun checkForDefaultAfterDelete() {
        if (customs.value.isNotEmpty()) {
            val listName = customs.value.first().listName
            storeHelper.setDefaultChannelList(name = listName)
        }
    }

    fun processAction(action: UserListAction) {
        when (action) {
            is UserListAction.AddList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    customListRepository.addCustomList(name = action.listName)
                    checkForDefaultAfterAdd()
                }
            }
            is UserListAction.DeleteList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    customListRepository.deleteList(item = action.list)
                    if (storeHelper.defaultChannelList == action.list.listName) {
                        checkForDefaultAfterDelete()
                    }
                }
            }
        }
    }
}
