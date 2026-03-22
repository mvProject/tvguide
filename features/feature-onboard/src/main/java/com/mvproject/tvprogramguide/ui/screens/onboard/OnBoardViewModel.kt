package com.mvproject.tvprogramguide.ui.screens.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardViewModel(
    private val preferenceRepository: IPreferenceRepository,
) : ViewModel() {

    fun completeOnBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.setOnBoardState(onBoardState = false)
        }
    }
}
