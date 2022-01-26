package com.mvproject.tvprogramguide.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.utils.Mappers.toSortedSingleChannelPrograms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleChannelProgramsViewModel @Inject constructor(
    private val channelProgramRepository: ChannelProgramRepository,
) : ViewModel() {

    private var _selectedPrograms = MutableStateFlow<List<IChannel>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    fun loadPrograms(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels =
                channelProgramRepository.loadChannelPrograms(id)

            val programs = programsWithChannels.toSortedSingleChannelPrograms()

            _selectedPrograms.emit(programs)
        }
    }
}
