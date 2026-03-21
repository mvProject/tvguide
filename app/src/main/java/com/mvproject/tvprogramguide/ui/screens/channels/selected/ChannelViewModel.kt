package com.mvproject.tvprogramguide.ui.screens.channels.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsWithProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.ui.screens.channels.selected.actions.ChannelsViewAction
import com.mvproject.tvprogramguide.ui.screens.channels.selected.state.ChannelsViewState
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChannelViewModel(
    private val channelListRepository: IChannelListRepository,
    private val selectedChannelsWithPrograms: GetSelectedChannelsWithProgramsUseCase,
    private val preferenceRepository: IPreferenceRepository,
    private val toggleProgramSchedule: ToggleProgramScheduleUseCase,
    private val selectChannelListUseCase: SelectChannelListUseCase,
) : ViewModel() {
    private var _viewState = MutableStateFlow(ChannelsViewState())
    val viewState = _viewState.asStateFlow()

    private var channelsJob: Job? = null

    init {
        combine(
            preferenceRepository.loadOnBoardState(),
            channelListRepository.loadChannelsListsAsFlow()
        ) { onboardState, allLists ->
            val listName = allLists.firstOrNull { it.isSelected }?.listName ?: String.empty
            Triple(onboardState, allLists.toImmutableList(), listName)
        }
            .flowOn(Dispatchers.IO)
            .onEach { (onboardState, playlists, listName) ->
                _viewState.update { state ->
                    state.copy(
                        isOnboard = onboardState,
                        listName = listName,
                        playlists = playlists,
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
        channelsJob?.cancel()
        channelsJob = selectedChannelsWithPrograms()
            .flowOn(Dispatchers.IO)
            .onEach { programs ->
                val sortedPrograms =
                    programs.sortedBy { item -> item.selectedChannel.order }.toImmutableList()

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
                state.copy(channels = selectedProgramsUpdated.toImmutableList())
            }
        }
    }
}
