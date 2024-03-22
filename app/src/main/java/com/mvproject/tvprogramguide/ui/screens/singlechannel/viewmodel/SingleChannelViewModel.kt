package com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannel
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.ui.screens.singlechannel.navigation.SingleChannelArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

        private var _selectedPrograms = MutableStateFlow<List<SingleChannelWithPrograms>>(emptyList())
        val selectedPrograms = _selectedPrograms.asStateFlow()

        val name get() = singleChannelArgs.channelName
        private val channelId get() = singleChannelArgs.channelId

        init {
            loadPrograms(channelId = channelId)
        }

        fun loadPrograms(channelId: String) {
            viewModelScope.launch(Dispatchers.IO) {
                val programsWithChannels = getProgramsByChannel(channelId = channelId)
                _selectedPrograms.emit(programsWithChannels)
            }
        }

        fun toggleSchedule(
            channelName: String,
            program: Program,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                toggleProgramSchedule(
                    channelName = channelName,
                    program = program,
                )

                loadPrograms(channelId = channelId)
            }
        }
    }
