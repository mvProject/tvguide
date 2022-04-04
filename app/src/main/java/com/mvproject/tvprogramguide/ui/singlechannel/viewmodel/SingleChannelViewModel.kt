package com.mvproject.tvprogramguide.ui.singlechannel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.SingleChannelModel
import com.mvproject.tvprogramguide.domain.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.domain.utils.Mappers.toSortedSingleChannelPrograms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleChannelViewModel @Inject constructor(
    private val channelProgramRepository: ChannelProgramRepository
) : ViewModel() {

    private var _selectedPrograms = MutableStateFlow<List<SingleChannelModel>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    init {
        Timber.i("testing SingleChannelViewModel init")
    }

    fun loadPrograms(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val programsWithChannels =
                channelProgramRepository.loadChannelPrograms(id)
            val programs = programsWithChannels.toSortedSingleChannelPrograms()
            _selectedPrograms.emit(programs)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("testing SingleChannelViewModel onCleared")
    }
}
