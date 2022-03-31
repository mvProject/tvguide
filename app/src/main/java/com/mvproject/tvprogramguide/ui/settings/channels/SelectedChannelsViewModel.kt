package com.mvproject.tvprogramguide.ui.settings.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.domain.repository.SelectedChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedChannelsViewModel @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val storeHelper: StoreHelper
) : ViewModel() {

    private var _selectedChannels = MutableStateFlow<List<Channel>>(emptyList())
    val selectedChannels = _selectedChannels.asStateFlow()

    private val listName = storeHelper.currentChannelList

    init {
        viewModelScope.launch {
            selectedChannelRepository.loadSelectedChannelsFlow(listName).collect {
                _selectedChannels.emit(it)
            }
        }
    }

    fun applyAction(action: SelectedChannelsAction){
        when(action){
            is SelectedChannelsAction.ChannelDelete ->{
                viewModelScope.launch(Dispatchers.IO) {
                    selectedChannelRepository.deleteChannel(action.channel.channelId)
                }
            }
        }
    }
}
