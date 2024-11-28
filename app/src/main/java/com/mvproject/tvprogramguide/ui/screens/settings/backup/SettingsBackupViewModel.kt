package com.mvproject.tvprogramguide.ui.screens.settings.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsBackupViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val _viewState = MutableStateFlow(BackupState(isUserLogged = auth.currentUser != null))
    val viewState = _viewState.asStateFlow()

    fun onAction(action: BackupAction) = when (action) {
        BackupAction.ProfileLogin -> login()
        BackupAction.ProfileLogout -> logout()
    }

    private fun login() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(isUserLogged = true)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(isUserLogged = false)
            }
        }
    }
}