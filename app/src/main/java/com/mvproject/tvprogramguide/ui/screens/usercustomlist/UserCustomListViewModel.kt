package com.mvproject.tvprogramguide.ui.screens.usercustomlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddPlaylistUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeletePlaylistUseCase
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.action.UserListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCustomListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {

    private var _customs = MutableStateFlow<List<UserChannelsList>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists()
                .collect { lists ->
                    _customs.value = lists
                }
        }
    }

    fun processAction(action: UserListAction) {
        when (action) {
            is UserListAction.AddList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addPlaylistUseCase(listName = action.listName)
                }

            }

            is UserListAction.DeleteList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deletePlaylistUseCase(list = action.list)
                }
            }
        }
    }
}
