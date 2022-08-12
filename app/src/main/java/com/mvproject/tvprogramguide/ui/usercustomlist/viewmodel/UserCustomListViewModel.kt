package com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCustomListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private var _customs = MutableStateFlow<List<UserChannelsList>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists()
                .collect { lists ->
                    _customs.value = lists
                    checkForDefaultAfterAdd()
                }
        }
    }

    private fun checkForDefaultAfterAdd() {
        if (customs.value.count() == COUNT_ONE) {
            val listName = customs.value.first().listName
            viewModelScope.launch(Dispatchers.IO) {
                preferenceRepository.setDefaultUserList(listName = listName)
            }
        }
    }

    private fun checkForDefaultAfterDelete() {
        if (customs.value.isNotEmpty()) {
            val listName = customs.value.first().listName
            viewModelScope.launch(Dispatchers.IO) {
                preferenceRepository.setDefaultUserList(listName = listName)
            }
        }
    }

    fun processAction(action: UserListAction) {
        when (action) {
            is UserListAction.AddList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    customListRepository.addCustomList(name = action.listName)
                }
            }
            is UserListAction.DeleteList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    customListRepository.deleteList(item = action.list)
                    val defaultChannelList = preferenceRepository.loadDefaultUserList().first()
                    if (defaultChannelList == action.list.listName) {
                        checkForDefaultAfterDelete()
                    }
                }
            }
        }
    }
}
