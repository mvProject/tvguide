package com.mvproject.tvprogramguide.ui.settings.channels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import com.mvproject.tvprogramguide.ui.settings.channels.actions.SelectedChannelsAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedChannelsViewModel @Inject constructor(
    private val selectedChannelUseCase: SelectedChannelUseCase,
) : ViewModel() {

    private var _selectedChannels = MutableStateFlow<List<SelectedChannel>>(emptyList())
    val selectedChannels = _selectedChannels.asStateFlow()

    init {
        viewModelScope.launch {
            selectedChannelUseCase.loadSelectedChannelsFlow()
                .collect { channels ->
                    _selectedChannels.emit(channels)
                }
        }
    }

    fun processAction(action: SelectedChannelsAction) {
        when (action) {
            is SelectedChannelsAction.ChannelDelete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedChannelUseCase.deleteChannelFromSelected(
                        channelId = action.selectedChannel.channelId
                    )
                }
            }
        }
    }
}
