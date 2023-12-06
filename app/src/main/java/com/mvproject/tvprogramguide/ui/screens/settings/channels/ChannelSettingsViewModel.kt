package com.mvproject.tvprogramguide.ui.screens.settings.channels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.SettingsChannelArgs
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ChannelSettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val settingsChannelArgs = SettingsChannelArgs(savedStateHandle)

    init {
        val name = settingsChannelArgs.userListName

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
