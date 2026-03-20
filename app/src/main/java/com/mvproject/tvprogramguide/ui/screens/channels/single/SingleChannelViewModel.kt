package com.mvproject.tvprogramguide.ui.screens.channels.single

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannelUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.SingleChannelArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleChannelViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProgramsByChannel: GetProgramsByChannelUseCase,
    private val toggleProgramSchedule: ToggleProgramScheduleUseCase,
) : ViewModel() {
    private val singleChannelArgs = SingleChannelArgs(savedStateHandle)

    val selectedPrograms = mutableStateListOf<SingleChannelWithPrograms>()

    val name get() = singleChannelArgs.channelName
    private val channelId get() = singleChannelArgs.channelId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels = getProgramsByChannel(channelId = channelId)

            selectedPrograms.apply {
                clear()
                addAll(programsWithChannels)
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

            val day = selectedPrograms.first { it.programs.contains(program) }
            val dayIndex = selectedPrograms.indexOf(day)
            val programIndex = day.programs.indexOf(program)
            val updatedPrograms =
                day.programs.toMutableList().also {
                    it[programIndex] = program.copy(scheduledId = scheduleId)
                }

            selectedPrograms[dayIndex] = day.copy(programs = updatedPrograms)
        }
    }
}
