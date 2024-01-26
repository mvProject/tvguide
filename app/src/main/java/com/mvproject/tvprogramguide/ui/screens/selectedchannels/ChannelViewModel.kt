package com.mvproject.tvprogramguide.ui.screens.selectedchannels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.state.AllPlaylists
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.state.ChannelsViewState
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.state.PlaylistContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val sortedProgramsUseCase: SortedProgramsUseCase,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private var _viewState = MutableStateFlow(ChannelsViewState())
    val viewState = _viewState.asStateFlow()

    init {
        combine(
            customListRepository.loadChannelsLists(),
            preferenceRepository.loadDefaultUserList(),
            preferenceRepository.loadChannelsUpdateRequired()
        ) { allLists, defaultList, _ ->
            updatePrograms()

            _viewState.update { current ->
                current.copy(
                    listName = defaultList,
                    allPlaylists = AllPlaylists(playlists = allLists),
                )
            }
        }.launchIn(viewModelScope)
    }

    fun reloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val isChannelsChanged = preferenceRepository.loadChannelsCountChanged().first()
            preferenceRepository.setChannelsUpdateRequired(isChannelsChanged)
            updatePrograms()
        }
    }

    fun applyList(listName: String) {
        viewModelScope.launch {
            preferenceRepository.setDefaultUserList(listName = listName)
        }
    }

    fun toggleProgramSchedule(programForSchedule: ProgramSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            sortedProgramsUseCase.updateProgramScheduleWithAlarm(
                programSchedule = programForSchedule
            )
            updatePrograms()
        }
    }

    private fun updatePrograms() {
        if (viewState.value.listName.isEmpty()) {
            Timber.e("testing no current saved list")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val programs = sortedProgramsUseCase.retrieveSelectedChannelWithPrograms()

                _viewState.update { current ->
                    current.copy(
                        isOnlineUpdating = false,
                        playlistContent = PlaylistContent(
                            channels = programs.sortedBy { item ->
                                item.selectedChannel.order
                            }
                        )
                    )
                }
            }
        }
    }
}
