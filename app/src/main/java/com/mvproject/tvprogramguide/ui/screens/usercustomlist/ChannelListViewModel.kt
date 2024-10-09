package com.mvproject.tvprogramguide.ui.screens.usercustomlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.action.ChannelListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val channelListRepository: ChannelListRepository,
    private val addChannelListUseCase: AddChannelListUseCase,
    private val deleteChannelListUseCase: DeleteChannelListUseCase
) : ViewModel() {

    private var _customs = MutableStateFlow<List<ChannelList>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            channelListRepository.loadChannelsListsAsFlow()
                .collect { lists ->
                    _customs.value = lists
                }
        }
    }

    fun processAction(action: ChannelListAction) {
        when (action) {
            is ChannelListAction.AddList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addChannelListUseCase(listName = action.listName)
                }

            }

            is ChannelListAction.DeleteList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteChannelListUseCase(list = action.list)
                }
            }
        }
    }
}
