package com.mvproject.tvprogramguide.customlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SelectedChannelsViewModel @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val storeManager: StoreManager
) : ViewModel() {

    private var _selectedChannels = MutableStateFlow<List<Channel>>(emptyList())
    val selectedChannels = _selectedChannels.asStateFlow()

    private val listName = storeManager.currentChannelList

    init {
        viewModelScope.launch {
            selectedChannelRepository.loadSelectedChannelsFlow(listName).collect {
                _selectedChannels.emit(it)
            }
        }
    }

    fun deleteList(item: Channel) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedChannelRepository.deleteChannel(item.channelId)
        }
    }
}
