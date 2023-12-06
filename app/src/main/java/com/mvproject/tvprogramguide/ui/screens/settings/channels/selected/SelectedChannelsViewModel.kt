package com.mvproject.tvprogramguide.ui.screens.settings.channels.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import com.mvproject.tvprogramguide.ui.screens.settings.channels.selected.action.SelectedChannelsAction
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
        viewModelScope.launch(Dispatchers.IO) {
            selectedChannelUseCase
                .loadSelectedChannelsFlow()
                .collect { channels ->
                    _selectedChannels.emit(
                        channels.sortedBy { chn ->
                            chn.order
                        }
                    )
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
            is SelectedChannelsAction.ChannelsReorder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedChannelUseCase.updateChannelsOrdersAfterReorder(
                        reorderedChannels = action.selectedChannels
                    )
                }
            }
        }
    }
}
