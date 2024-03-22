package com.mvproject.tvprogramguide.ui.screens.settings.channels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.domain.usecases.GetAvailableChannels
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannels
import com.mvproject.tvprogramguide.domain.usecases.SaveChannelsSelection
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.SettingsChannelArgs
import com.mvproject.tvprogramguide.ui.screens.settings.channels.state.ChannelSettingsState
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.ChannelUtils.updateOrders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChannelSettingsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getSelectedChannels: GetSelectedChannels,
        private val getAvailableChannels: GetAvailableChannels,
        private val saveChannelsSelection: SaveChannelsSelection,
    ) : ViewModel() {
        private val _viewState = MutableStateFlow(ChannelSettingsState())
        val viewState = _viewState.asStateFlow()

        val allChannels = mutableStateListOf<SelectionChannel>()

        private val _selected = MutableStateFlow<List<SelectionChannel>>(emptyList())
        val selected = _selected.asStateFlow()

        var name = SettingsChannelArgs(savedStateHandle).userListName
            private set

        private var isRefreshRequired = false

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
            isRefreshRequired = true
        }

        private fun removeFromSelected(channel: SelectionChannel) {
            val updated = channel.copy(isSelected = false)

            val index = allChannels.indexOfFirst { it.channelId == channel.channelId }

            allChannels.set(
                index = index,
                element = updated,
            )

            _selected.value = removeChannel(removeId = channel.channelId)
        }

        private fun removeChannel(removeId: String): List<SelectionChannel> {
            Timber.w("testing remove $removeId")
            val modified =
                selected.value.toMutableList()
                    .apply {
                        removeIf {
                            it.channelId == removeId
                        }
                    }
            modified.forEach {
                Timber.d("testing modified $it")
            }

            return modified.updateOrders()
        }

        fun applyChanges() {
            viewModelScope.launch {
                Timber.d("testing applyChanges")
                saveChannelsSelection(
                    listName = name,
                    channels = selected.value,
                    isUpdateRequired = isRefreshRequired,
                )
                Timber.w("testing applyChanges complete")
                _viewState.update { state ->
                    state.copy(isComplete = true)
                }
            }
        }
    }
