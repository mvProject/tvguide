package com.mvproject.tvprogramguide.ui.screens.selectedchannels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelsWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.state.ChannelsViewState
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChannelViewModel
    @Inject
    constructor(
        private val workManager: WorkManager,
        private val customListRepository: CustomListRepository,
        private val selectedChannelsWithPrograms: SelectedChannelsWithPrograms,
        private val preferenceRepository: PreferenceRepository,
        private val toggleProgramSchedule: ToggleProgramSchedule,
    ) : ViewModel() {
        private var _viewState = MutableStateFlow(ChannelsViewState())
        val viewState = _viewState.asStateFlow()

        val selectedPrograms = mutableStateListOf<SelectedChannelWithPrograms>()

        private val fullUpdateWorkInfoFlow =
            workManager.getWorkInfosForUniqueWorkFlow(DOWNLOAD_PROGRAMS)

        init {
            combine(
                customListRepository.loadChannelsLists(),
                preferenceRepository.loadDefaultUserList(),
            ) { allLists, defaultList ->
                _viewState.update { current ->
                    current.copy(
                        listName = defaultList,
                        playlists = allLists,
                    )
                }
            }.launchIn(viewModelScope)

            fullUpdateWorkInfoFlow.onEach { state ->
                if (!state.isNullOrEmpty()) {
                    val workInfo = state.first()
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        if (viewState.value.listName.isNotBlank()) {
                            updatePrograms()
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun reloadData() {
            viewModelScope.launch(Dispatchers.IO) {
                val isChannelsChanged = preferenceRepository.loadChannelsCountChanged()
                requestUpdate(isOnlineRequested = isChannelsChanged)
            }
        }

        fun forceReloadData() {
            viewModelScope.launch(Dispatchers.IO) {
                requestUpdate()
            }
        }

        private suspend fun requestUpdate(isOnlineRequested: Boolean = true) {
            _viewState.update { state ->
                state.copy(isLoading = true)
            }
            preferenceRepository.setChannelsUpdateRequired(isOnlineRequested)
            updatePrograms()
        }

        fun applyList(listName: String) {
            if (listName.isNotBlank()) {
                viewModelScope.launch {
                    preferenceRepository.setDefaultUserList(listName = listName)
                }
            }
        }

        fun toggleSchedule(
            channelName: String,
            program: Program,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val scheduleId =
                    toggleProgramSchedule(
                        channelName = channelName,
                        program = program,
                    )

                val channel = selectedPrograms.first { it.programs.contains(program) }
                val channelIndex = selectedPrograms.indexOf(channel)

                val updatedPrograms =
                    channel.programs.toMutableList().also {
                        val programIndex = it.indexOf(program)
                        it[programIndex] = program.copy(scheduledId = scheduleId)
                    }

                selectedPrograms[channelIndex] = channel.copy(programs = updatedPrograms)
            }
        }

        private fun updatePrograms() {
            if (viewState.value.listName.isEmpty()) {
                Timber.e("testing no current saved list")
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val programs =
                        selectedChannelsWithPrograms()
                            .sortedBy { item ->
                                item.selectedChannel.order
                            }

                    selectedPrograms.apply {
                        clear()
                        addAll(programs)
                    }

                    _viewState.update { state ->
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }
