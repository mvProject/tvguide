package com.mvproject.tvprogramguide.ui.screens.channels.single

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannelUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.SingleChannelArgs
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SingleChannelViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProgramsByChannel: GetProgramsByChannelUseCase,
    private val toggleProgramSchedule: ToggleProgramScheduleUseCase,
) : ViewModel() {
    private val singleChannelArgs = SingleChannelArgs(savedStateHandle)

    private val _selectedPrograms =
        MutableStateFlow<ImmutableList<SingleChannelWithPrograms>>(persistentListOf())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    val name get() = singleChannelArgs.channelName
    private val channelId get() = singleChannelArgs.channelId

    init {
        reloadPrograms()
    }

    fun reloadPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels = getProgramsByChannel(channelId = channelId)

            _selectedPrograms.value = programsWithChannels.toImmutableList()
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

            _selectedPrograms.update { current ->
                val day = current.first { it.programs.contains(program) }
                val dayIndex = current.indexOf(day)
                val programIndex = day.programs.indexOf(program)
                val updatedPrograms = day.programs.toMutableList().also {
                    it[programIndex] = program.copy(scheduledId = scheduleId)
                }
                current.toMutableList().also {
                    it[dayIndex] = day.copy(programs = updatedPrograms)
                }.toImmutableList()
            }
        }
    }
}
