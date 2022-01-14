package com.mvproject.tvprogramguide.customlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.repository.AllChannelRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.COUNT_ONE
import com.mvproject.tvprogramguide.utils.Mappers.asAlreadySelected
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChannelViewModel @Inject constructor(
    private val allChannelRepository: AllChannelRepository,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val storeHelper: StoreHelper
) : ViewModel() {

    private var _allChannels = MutableStateFlow<List<Channel>>(emptyList())
    val allChannels = _allChannels.asStateFlow()

    private var _selectedChannels = MutableStateFlow<List<Channel>>(emptyList())
    val selectedChannels = _selectedChannels.asStateFlow()

    private val listName = storeHelper.currentChannelList

    private var queryText = NO_VALUE_STRING

    init {
        viewModelScope.launch {
            selectedChannelRepository.loadSelectedChannelsFlow(listName).collect {
                _selectedChannels.emit(it)
            }
        }
    }

    fun reloadAllChannels() = viewModelScope.launch {
        updateChannelsData()
    }

    fun applyChannelAction(item: Channel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.isSelected) {
                selectedChannelRepository.deleteChannel(item.channelId)
            } else {
                val selected = SelectedChannelEntity(
                    item.channelId,
                    item.channelName,
                    item.channelIcon,
                    listName
                )
                selectedChannelRepository.addChannel(selected)
            }
        }
    }

    fun filterByQuery(query: String) {
        queryText = query
        reloadAllChannels()
    }

    private fun performQuery(data: List<Channel>): List<Channel> {
        if (queryText.length > COUNT_ONE) {
            return data.filter { it.channelName.contains(queryText, true) }
        }
        return data
    }

    private suspend fun updateChannelsData() {
        val alreadySelected = selectedChannels.value.map { it.channelName }
        val all = allChannelRepository.loadChannels()
            .map { it.asAlreadySelected(it.channelName in alreadySelected) }
        _allChannels.emit(performQuery(all))
    }
}
