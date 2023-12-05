package com.mvproject.tvprogramguide.ui.screens.settings.channels

import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    fun setSelectedList(name: String) {
        runBlocking {
            preferenceRepository.setCurrentUserList(listName = name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        runBlocking {
            preferenceRepository.setCurrentUserList(listName = NO_VALUE_STRING)
        }
    }
}
