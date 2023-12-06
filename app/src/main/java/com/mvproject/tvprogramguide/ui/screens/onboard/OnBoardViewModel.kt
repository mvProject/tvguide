package com.mvproject.tvprogramguide.ui.screens.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    fun completeOnBoard(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setOnBoardState(onBoardState = completed)
        }
    }
}
