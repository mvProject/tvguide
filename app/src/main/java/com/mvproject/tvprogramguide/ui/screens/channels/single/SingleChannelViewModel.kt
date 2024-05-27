package com.mvproject.tvprogramguide.ui.screens.channels.single

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannel
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.SingleChannelArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleChannelViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getProgramsByChannel: GetProgramsByChannel,
        private val toggleProgramSchedule: ToggleProgramSchedule,
    ) : ViewModel() {
        private val singleChannelArgs = SingleChannelArgs(savedStateHandle)

        val selectedPrograms = mutableStateListOf<SingleChannelWithPrograms>()

        val name get() = singleChannelArgs.channelName
        private val channelId get() = singleChannelArgs.channelId

        init {
            Timber.e("testing SingleChannelViewModel channelId ${singleChannelArgs.channelId}")
            Timber.e("testing SingleChannelViewModel channelName ${singleChannelArgs.channelName}")
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
