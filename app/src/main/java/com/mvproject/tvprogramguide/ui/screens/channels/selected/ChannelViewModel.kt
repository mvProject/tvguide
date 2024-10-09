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
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

   // private var _channelsState =
   //     MutableStateFlow<List<SelectedChannelWithPrograms>>(emptyList())
   // val channelsState: StateFlow<List<SelectedChannelWithPrograms>> = _channelsState


    init {
        preferenceRepository.loadOnBoardState().onEach { onboardState ->
            _viewState.update { state ->
                state.copy(
                    isOnboard = onboardState
                )
            }

        }.launchIn(viewModelScope)

        channelListRepository.loadChannelsListsAsFlow().onEach { allLists ->
            _viewState.update { state ->
                val listName = allLists.firstOrNull { it.isSelected }?.listName ?: NO_VALUE_STRING

                state.copy(
                    listName = listName,
                    playlists = allLists,
                )
            }

        }.launchIn(viewModelScope)

        selectedChannelsWithPrograms().onEach { programs ->
            val sortedPrograms = programs.sortedBy { item -> item.selectedChannel.order }
          //  _channelsState.value = sortedPrograms

            _viewState.update { state ->
               state.copy(channels = sortedPrograms)
            }

        }.launchIn(viewModelScope)
    }

    fun processAction(action: ChannelsViewAction) {
        when (action) {
            ChannelsViewAction.RefreshChannels -> reloadData()
            ChannelsViewAction.ReloadChannels -> forceReloadData()
            is ChannelsViewAction.SelectChannelList -> applyList(list = action.list)
            ChannelsViewAction.CompleteOnBoard -> completeOnBoard()
            is ChannelsViewAction.ToggleScheduleProgram -> toggleSchedule(
                channelName = action.channelName,
                program = action.program
            )
        }
    }

    private fun reloadData() {
        // todo refresh channels
    }

    private fun completeOnBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setOnBoardState(onBoardState = false)
        }
    }

    private fun forceReloadData() {
      //  val currentChannels = channelsState.value.map { it.selectedChannel.programId }
        val currentChannels = viewState.value.channels.map { it.selectedChannel.programId }
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setChannelsForUpdate(currentChannels)
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
           // val channel = channelsState.value.first { it.programs.contains(program) }
            val channelIndex = currentChannels.indexOf(channel)
           // val channelIndex = channelsState.value.indexOf(channel)

            val updatedPrograms =
                channel.programs.toMutableList().also {
                    val programIndex = it.indexOf(program)
                    it[programIndex] = program.copy(scheduledId = scheduleId)
                }
           // val selectedProgramsUpdated = channelsState.value.toMutableList()
            val selectedProgramsUpdated = currentChannels.toMutableList()

            selectedProgramsUpdated[channelIndex] = channel.copy(programs = updatedPrograms)

        //    _channelsState.value = selectedProgramsUpdated
            _viewState.update { state ->
                state.copy(channels = selectedProgramsUpdated)
            }
        }
    }
}
