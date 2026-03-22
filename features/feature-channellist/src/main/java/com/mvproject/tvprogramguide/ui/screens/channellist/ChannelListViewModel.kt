package com.mvproject.tvprogramguide.ui.screens.channellist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.ui.screens.channellist.action.ChannelListAction
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChannelListViewModel(
    private val channelListRepository: IChannelListRepository,
    private val addChannelListUseCase: AddChannelListUseCase,
    private val deleteChannelListUseCase: DeleteChannelListUseCase,
    private val selectChannelListUseCase: SelectChannelListUseCase,
) : ViewModel() {

    /*    private var _customs = MutableStateFlow<List<ChannelList>>(emptyList())
        val customs = _customs.asStateFlow()*/

    val customs by lazy {
        channelListRepository.loadChannelsListsAsFlow()
            .map { it.toImmutableList() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(3000L),
                persistentListOf()
            )
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
