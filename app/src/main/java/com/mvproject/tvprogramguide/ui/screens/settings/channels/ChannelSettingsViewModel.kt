package com.mvproject.tvprogramguide.ui.screens.settings.channels

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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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

    private val _allChannels = MutableStateFlow<ImmutableList<SelectionChannel>>(persistentListOf())
    val allChannels = _allChannels.asStateFlow()

    private val _selected = MutableStateFlow<ImmutableList<SelectionChannel>>(persistentListOf())
    val selected = _selected.asStateFlow()

    var name = SettingsChannelArgs(savedStateHandle).userListName
        private set

    private val channelsForUpdate = mutableListOf<String>()

    init {
        viewModelScope.launch {
            _selected.value = getSelectedChannels(listName = name).toImmutableList()
        }

        viewModelScope.launch {
            val available = getAvailableChannels(listName = name)
            _allChannels.value = available.toImmutableList()
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
                _selected.value = action.selectedChannels.updateOrders().toImmutableList()
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

        _selected.value = selected.value.plus(updated).toImmutableList()

        _allChannels.update { list ->
            list.toMutableList()
                .also { it[it.indexOfFirst { c -> c.channelId == channel.channelId }] = updated }
                .toImmutableList()
        }

        channelsForUpdate.add(channel.programId)
    }

    private fun removeFromSelected(channel: SelectionChannel) {
        val updated = channel.copy(isSelected = false)

        _allChannels.update { list ->
            list.toMutableList()
                .also { it[it.indexOfFirst { c -> c.channelId == channel.channelId }] = updated }
                .toImmutableList()
        }

        _selected.value = removeChannel(removeId = channel.channelId)
        if (channelsForUpdate.isNotEmpty()) {
            val updateIndex = channelsForUpdate.indexOf(channel.programId)
            channelsForUpdate.removeAt(updateIndex)
        }
    }

    private fun removeChannel(removeId: String): ImmutableList<SelectionChannel> {
        val modified =
            selected.value
                .toMutableList()
                .apply {
                    removeIf {
                        it.channelId == removeId
                    }
                }
        return modified.updateOrders().toImmutableList()
    }

    fun applyChanges() {
        viewModelScope.launch(Dispatchers.IO) {
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
