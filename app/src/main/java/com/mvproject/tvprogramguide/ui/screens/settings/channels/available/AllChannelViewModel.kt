package com.mvproject.tvprogramguide.ui.screens.settings.channels.available

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import com.mvproject.tvprogramguide.ui.screens.settings.channels.available.action.AvailableChannelsAction
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AllChannelViewModel
    @Inject
    constructor(
        private val allChannelRepository: AllChannelRepository,
        private val selectedChannelUseCase: SelectedChannelUseCase,
        private val preferenceRepository: PreferenceRepository,
    ) : ViewModel() {
        private var _availableChannels = MutableStateFlow<List<AvailableChannel>>(emptyList())
        val availableChannels = _availableChannels.asStateFlow()

        private var queryText = NO_VALUE_STRING
        private var isRefreshRequired = false

        private var allChannels = emptyList<AvailableChannel>()
        private var selectedChannels = emptyList<SelectedChannel>()

        init {
            viewModelScope.launch(Dispatchers.IO) {
                selectedChannelUseCase.loadSelectedChannelsFlow()
                    .collect { channels ->
                        allChannels = allChannelRepository.loadChannels()
                        selectedChannels = channels
                        updateChannelsData()
                    }
            }
        }

        fun processAction(action: AvailableChannelsAction) {
            when (action) {
                is AvailableChannelsAction.ChannelAdd -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        selectedChannelUseCase.addChannelToSelected(
                            selectedChannel = action.selectedChannel,
                        )
                        isRefreshRequired = true
                    }
                }

                is AvailableChannelsAction.ChannelDelete -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        selectedChannelUseCase.deleteChannelFromSelected(
                            channelId = action.selectedChannel.channelId,
                        )
                    }
                }

                is AvailableChannelsAction.ChannelFilter -> {
                    queryText = action.query
                    viewModelScope.launch(Dispatchers.IO) {
                        updateChannelsData()
                    }
                }
            }
        }

        private fun performQuery(data: List<AvailableChannel>): List<AvailableChannel> {
            if (queryText.length > COUNT_ONE) {
                return data.filter { channel ->
                    channel.channelName.contains(queryText, true)
                }
            }
            return data
        }

        private suspend fun updateChannelsData() {
            val alreadySelected =
                selectedChannels.map { channel ->
                    channel.channelName
                }
            val filtered =
                allChannels.map { channel ->
                    channel.copy(isSelected = channel.channelName in alreadySelected)
                }
            _availableChannels.emit(performQuery(filtered))
        }

        fun requestUpdate() {
            Timber.w("testing requestUpdate")
            viewModelScope.launch {
                preferenceRepository.setChannelsCountChanged(isRefreshRequired)
            }
        }

        override fun onCleared() {
            super.onCleared()
            Timber.w("testing onCleared")
            runBlocking {
                preferenceRepository.setChannelsCountChanged(isRefreshRequired)
            }
        }
    }
