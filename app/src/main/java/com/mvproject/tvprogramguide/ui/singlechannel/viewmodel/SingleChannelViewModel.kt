package com.mvproject.tvprogramguide.ui.singlechannel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.toSingleChannelWithPrograms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleChannelViewModel @Inject constructor(
    private val channelProgramRepository: ChannelProgramRepository
) : ViewModel() {

    private var _selectedPrograms = MutableStateFlow<List<SingleChannelWithPrograms>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    fun loadPrograms(channelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels =
                channelProgramRepository
                    .loadProgramsForChannel(channelId = channelId)
                    .toSingleChannelWithPrograms()

            _selectedPrograms.emit(programsWithChannels)
        }
    }
}
