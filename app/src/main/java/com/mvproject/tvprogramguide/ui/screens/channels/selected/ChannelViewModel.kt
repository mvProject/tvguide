package com.mvproject.tvprogramguide.ui.screens.channels.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelsWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.channels.selected.state.ChannelsViewState
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    private val channelListRepository: ChannelListRepository,
    private val selectedChannelsWithPrograms: SelectedChannelsWithPrograms,
    private val preferenceRepository: PreferenceRepository,
    private val toggleProgramSchedule: ToggleProgramSchedule,
    private val selectChannelListUseCase: SelectChannelListUseCase,
) : ViewModel() {
    private var _viewState = MutableStateFlow(ChannelsViewState())
    val viewState = _viewState.asStateFlow()

    private var _selectedProgramsState =
        MutableStateFlow<List<SelectedChannelWithPrograms>>(emptyList())
    val selectedProgramsState = _selectedProgramsState.asStateFlow()

    private val fullUpdateWorkInfoFlow =
        workManager.getWorkInfosForUniqueWorkFlow(DOWNLOAD_PROGRAMS)

    init {
        channelListRepository.loadChannelsListsAsFlow().onEach { allLists ->
            _viewState.update { current ->

                val listName = allLists.firstOrNull { it.isSelected }?.listName ?: NO_VALUE_STRING
                Timber.w("testing listName $listName")

                current.copy(
                    listName = listName,
                    playlists = allLists,
                    isOnboard = preferenceRepository.loadOnBoardState().first(),
                    isLoading = false,
                )
            }

        }.launchIn(viewModelScope)


        fullUpdateWorkInfoFlow
            .onEach { state ->
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
        requestUpdate()
    }

    fun completeOnBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setOnBoardState(onBoardState = false)
            _viewState.update { state ->
                state.copy(isOnboard = false)
            }
        }
    }

    fun forceReloadData() {
        requestUpdate(ids = selectedProgramsState.value.map { it.selectedChannel.programId })
    }

    private fun requestUpdate(ids: List<String> = emptyList()) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.update { state ->
                state.copy(isLoading = true)
            }
            preferenceRepository.setChannelsForUpdate(ids)
            updatePrograms()
        }
    }

    fun applyList(list: ChannelList) {
        if (list.listName.isNotBlank()) {
            viewModelScope.launch {
                selectChannelListUseCase(list = list)
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

            val channel = selectedProgramsState.value.first { it.programs.contains(program) }
            val channelIndex = selectedProgramsState.value.indexOf(channel)

            val updatedPrograms =
                channel.programs.toMutableList().also {
                    val programIndex = it.indexOf(program)
                    it[programIndex] = program.copy(scheduledId = scheduleId)
                }
            val selectedProgramsUpdated = selectedProgramsState.value.toMutableList()

            selectedProgramsUpdated[channelIndex] = channel.copy(programs = updatedPrograms)
            _selectedProgramsState.value = selectedProgramsUpdated
        }
    }

    private fun updatePrograms() {
        if (viewState.value.listName.isEmpty()) {
            Timber.e("testing no current saved list")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val programs =
                    selectedChannelsWithPrograms()
                        .sortedBy { item -> item.selectedChannel.order }

                _selectedProgramsState.value = programs

                _viewState.update { state ->
                    state.copy(isLoading = false)
                }
            }
        }
    }
}
