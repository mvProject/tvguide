package com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.ui.screens.singlechannel.navigation.SingleChannelArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleChannelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sortedProgramsUseCase: SortedProgramsUseCase,
) : ViewModel() {
    private val singleChannelArgs = SingleChannelArgs(savedStateHandle)

    private var _selectedPrograms = MutableStateFlow<List<SingleChannelWithPrograms>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    val name get() = singleChannelArgs.channelName

    init {
        loadPrograms(channelId = singleChannelArgs.channelId)
    }

    fun loadPrograms(channelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels = sortedProgramsUseCase
                .retrieveProgramsForChannel(channelId = channelId)

            _selectedPrograms.emit(programsWithChannels)
        }
    }

    fun toggleProgramSchedule(programForSchedule: ProgramSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            sortedProgramsUseCase
                .updateProgramScheduleWithAlarm(programSchedule = programForSchedule)
            loadPrograms(channelId = programForSchedule.channelId)
        }
    }
}
