package com.mvproject.tvprogramguide.ui.settings.channels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.data.utils.Mappers.asAlreadySelected
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import com.mvproject.tvprogramguide.ui.settings.channels.actions.AvailableChannelsAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChannelViewModel @Inject constructor(
    private val allChannelRepository: AllChannelRepository,
    private val selectedChannelUseCase: SelectedChannelUseCase,
) : ViewModel() {

    private var _availableChannels = MutableStateFlow<List<AvailableChannel>>(emptyList())
    val availableChannels = _availableChannels.asStateFlow()

    private var _selectedChannels = MutableStateFlow<List<SelectedChannel>>(emptyList())
    val selectedChannels = _selectedChannels.asStateFlow()

    private var queryText = NO_VALUE_STRING

    private var allChannels = emptyList<AvailableChannel>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            selectedChannelUseCase.loadSelectedChannelsFlow()
                .collect { channels ->
                    allChannels = allChannelRepository.loadChannels()
                    _selectedChannels.emit(channels)
                    reloadAllChannels()
                }
        }
    }

    private fun reloadAllChannels() = viewModelScope.launch {
        updateChannelsData()
    }

    fun processAction(action: AvailableChannelsAction) {
        when (action) {
            is AvailableChannelsAction.ChannelAdd -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedChannelUseCase
                        .addChannelToSelected(
                            selectedChannel = action.selectedChannel
                        )
                }
            }
            is AvailableChannelsAction.ChannelDelete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedChannelUseCase
                        .deleteChannelFromSelected(
                            channelId = action.selectedChannel.channelId
                        )
                }
            }
            is AvailableChannelsAction.ChannelFilter -> {
                queryText = action.query
                reloadAllChannels()
            }
        }
    }

    private fun performQuery(data: List<AvailableChannel>): List<AvailableChannel> {
        if (queryText.length > COUNT_ONE) {
            return data.filter { channel ->
                channel.channelName
                    .contains(queryText, true)
            }
        }
        return data
    }

    private suspend fun updateChannelsData() {
        val alreadySelected = selectedChannels.value.map { channel ->
            channel.channelName
        }
        val filtered = allChannels
            .map { channel ->
                channel.asAlreadySelected(
                    state = channel.channelName in alreadySelected
                )
            }
        _availableChannels.emit(performQuery(filtered))
    }
}
