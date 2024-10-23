package com.mvproject.tvprogramguide.ui.screens.channels.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelsWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.channels.selected.actions.ChannelsViewAction
import com.mvproject.tvprogramguide.ui.screens.channels.selected.state.ChannelsViewState
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChannelViewModel
@Inject
constructor(
    private val channelListRepository: ChannelListRepository,
    private val selectedChannelsWithPrograms: SelectedChannelsWithPrograms,
    private val preferenceRepository: PreferenceRepository,
    private val toggleProgramSchedule: ToggleProgramSchedule,
    private val selectChannelListUseCase: SelectChannelListUseCase,
) : ViewModel() {
    private var _viewState = MutableStateFlow(ChannelsViewState())
    val viewState = _viewState.asStateFlow()

    private var channelsJob: Job? = null

    init {
        preferenceRepository.loadOnBoardState()
            .flowOn(Dispatchers.IO)
            .onEach { onboardState ->
                _viewState.update { state ->
                    state.copy(isOnboard = onboardState)
                }

            }.launchIn(viewModelScope)

        channelListRepository.loadChannelsListsAsFlow()
            .flowOn(Dispatchers.IO)
            .onEach { allLists ->

                _viewState.update { state ->
                    val listName =
                        allLists.firstOrNull { it.isSelected }?.listName ?: String.empty

                    state.copy(
                        listName = listName,
                        playlists = allLists,
                        isLoading = listName.isNotEmpty()
                    )
                }

            }.launchIn(viewModelScope)
    }

    fun processAction(action: ChannelsViewAction) {
        when (action) {
            ChannelsViewAction.StartUpdates -> startProgramsObserving()
            ChannelsViewAction.StopUpdates -> stopProgramsObserving()
            ChannelsViewAction.ReloadChannels -> forceReloadData()
            is ChannelsViewAction.SelectChannelList -> applyList(list = action.list)
            ChannelsViewAction.CompleteOnBoard -> completeOnBoard()
            is ChannelsViewAction.ToggleScheduleProgram -> toggleSchedule(
                channelName = action.channelName,
                program = action.program
            )
        }
    }

    private fun startProgramsObserving() {
        channelsJob = selectedChannelsWithPrograms()
            .flowOn(Dispatchers.IO)
            .onEach { programs ->
                val sortedPrograms = programs.sortedBy { item -> item.selectedChannel.order }

                _viewState.update { state ->
                    state.copy(
                        channels = sortedPrograms,
                        isLoading = false
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun stopProgramsObserving() {
        channelsJob?.cancel()
        channelsJob = null
    }


    private fun completeOnBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setOnBoardState(onBoardState = false)
        }
    }

    private fun forceReloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setProgramsUpdateRequiredState(true)
        }
    }

    private fun applyList(list: ChannelList) {
        if (list.listName.isNotBlank()) {
            viewModelScope.launch {
                selectChannelListUseCase(list = list)
            }
        }
    }

    private fun toggleSchedule(
        channelName: String,
        program: Program,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleId =
                toggleProgramSchedule(
                    channelName = channelName,
                    program = program,
                )

            val currentChannels = viewState.value.channels
            val channel = currentChannels.first { it.programs.contains(program) }
            val channelIndex = currentChannels.indexOf(channel)

            val updatedPrograms =
                channel.programs.toMutableList().also {
                    val programIndex = it.indexOf(program)
                    it[programIndex] = program.copy(scheduledId = scheduleId)
                }
            val selectedProgramsUpdated = currentChannels.toMutableList()

            selectedProgramsUpdated[channelIndex] = channel.copy(programs = updatedPrograms)

            _viewState.update { state ->
                state.copy(channels = selectedProgramsUpdated)
            }
        }
    }
}
