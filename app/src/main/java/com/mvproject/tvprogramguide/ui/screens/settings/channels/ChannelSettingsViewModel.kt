package com.mvproject.tvprogramguide.ui.screens.settings.channels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.GetAvailableChannelsUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsUseCase
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.SettingsChannelArgs
import com.mvproject.tvprogramguide.ui.screens.settings.channels.state.ChannelSettingsState
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.ChannelUtils.updateOrders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChannelSettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSelectedChannels: GetSelectedChannelsUseCase,
    private val getAvailableChannels: GetAvailableChannelsUseCase,
    private val selectedChannelRepository: ISelectedChannelRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(ChannelSettingsState())
    val viewState = _viewState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000L),
        ChannelSettingsState()
    )

    val allChannels = mutableStateListOf<SelectionChannel>()

    private val _selected = MutableStateFlow<List<SelectionChannel>>(emptyList())
    val selected = _selected.asStateFlow()

    var name = SettingsChannelArgs(savedStateHandle).userListName
        private set

    private val channelsForUpdate = mutableListOf<String>()

    init {
        viewModelScope.launch {
            _selected.value = getSelectedChannels(listName = name)
        }

        viewModelScope.launch {
            val available = getAvailableChannels(listName = name)
            allChannels.apply {
                clear()
                addAll(available)
            }
        }
    }

    fun processAction(action: ChannelsAction) {
        when (action) {
            is ChannelsAction.DeleteSelection -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeFromSelected(action.selectedChannel)
                }
            }

            is ChannelsAction.ChannelsReorder -> {
                _selected.value = action.selectedChannels.updateOrders()
            }

            is ChannelsAction.ChannelFilter -> {
                _viewState.update { current ->
                    current.copy(searchString = action.query)
                }
            }

            is ChannelsAction.ToggleSelection -> {
                val current = action.channel
                val favIds = selected.value.map { it.channelId }

                if (current.channelId in favIds) {
                    removeFromSelected(channel = current)
                } else {
                    addToSelected(channel = current)
                }
            }
        }
    }

    private fun addToSelected(channel: SelectionChannel) {
        val order = selected.value.size + COUNT_ONE
        val updated = channel.copy(isSelected = true, order = order)

        _selected.value = selected.value.plus(updated)

        allChannels.set(
            index = allChannels.indexOf(channel),
            element = updated,
        )

        channelsForUpdate.add(channel.programId)
    }

    private fun removeFromSelected(channel: SelectionChannel) {
        val updated = channel.copy(isSelected = false)

        val index = allChannels.indexOfFirst { it.channelId == channel.channelId }

        allChannels.set(
            index = index,
            element = updated,
        )

        _selected.value = removeChannel(removeId = channel.channelId)
        if (channelsForUpdate.isNotEmpty()) {
            val updateIndex = channelsForUpdate.indexOf(channel.programId)
            channelsForUpdate.removeAt(updateIndex)
        }
    }

    private fun removeChannel(removeId: String): List<SelectionChannel> {
        val modified =
            selected.value
                .toMutableList()
                .apply {
                    removeIf {
                        it.channelId == removeId
                    }
                }
        return modified.updateOrders()
    }

    fun applyChanges() {
        viewModelScope.launch {
            selectedChannelRepository.addChannels(
                listName = name,
                selectedChannels = selected.value,
            )
            _viewState.update { state ->
                state.copy(isComplete = true)
            }
        }
    }
}
