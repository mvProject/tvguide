package com.mvproject.tvprogramguide.ui.screens.channellist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.ui.screens.channellist.action.ChannelListAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChannelListViewModel(
    private val channelListRepository: ChannelListRepository,
    private val addChannelListUseCase: AddChannelListUseCase,
    private val deleteChannelListUseCase: DeleteChannelListUseCase,
    private val selectChannelListUseCase: SelectChannelListUseCase,
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

            is ChannelListAction.SelectList -> {
                viewModelScope.launch {
                    selectChannelListUseCase(list = action.list)
                }
            }
        }
    }
}
