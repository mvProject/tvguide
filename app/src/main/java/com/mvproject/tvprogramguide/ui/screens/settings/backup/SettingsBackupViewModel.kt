package com.mvproject.tvprogramguide.ui.screens.settings.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.data.repository.BackupRepository
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.domain.usecases.BackupCreateUseCase
import com.mvproject.tvprogramguide.domain.usecases.BackupRestoreUseCase
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupData
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupState
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.RestoreData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsBackupViewModel(
    private val channelListRepository: ChannelListRepository,
    private val backupRepository: BackupRepository,
    private val backupCreateUseCase: BackupCreateUseCase,
    private val backupRestoreUseCase: BackupRestoreUseCase
) : ViewModel() {
    private val auth = Firebase.auth

    private val _viewState = MutableStateFlow(BackupState(isUserLogged = auth.currentUser != null))
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {

            val backupData = channelListRepository
                .loadChannelsLists()
                .map { BackupData(name = it.listName) }

            _viewState.update {
                it.copy(backupData = backupData)
            }
        }
    }

    fun onAction(action: BackupAction) = when (action) {
        BackupAction.ProfileLogin -> login()
        BackupAction.ProfileLogout -> logout()
        is BackupAction.SetBackupMode -> setBackupMode(mode = action.mode)
        is BackupAction.SelectForBackup -> selectForBackup(name = action.name)
        BackupAction.CreateBackup -> createBackup()
        BackupAction.RestoreBackup -> restoreBackup()
        BackupAction.SelectForRestore -> selectForRestore()
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

    private fun createBackup() {
        viewModelScope.launch {
            val lists = viewState.value.backupData.filter { it.isSelected }.map { it.name }
            val tvBackup = backupCreateUseCase(lists = lists)

            backupRepository.saveBackup(tvBackup = tvBackup)
        }
    }

    private fun restoreBackup() {
        viewModelScope.launch {
            backupRestoreUseCase(tvBackup = viewState.value.restoreData.tvBackup)
        }
    }

    private fun selectForBackup(name: String) {
        viewModelScope.launch {
            _viewState.update { state ->
                val playlists = state.backupData.map { list ->
                    val selection = if (list.name == name) !list.isSelected else list.isSelected
                    list.copy(name = list.name, isSelected = selection)
                }
                state.copy(backupData = playlists)
            }
        }
    }

    private fun selectForRestore() {
        viewModelScope.launch {
            _viewState.update { state ->
                val tvBackup = state.restoreData
                    .copy(isSelected = !state.restoreData.isSelected)
                state.copy(restoreData = tvBackup)
            }
        }
    }

    private fun setBackupMode(mode: BackupState.BackupMode) {
        viewModelScope.launch {

            when (mode) {
                BackupState.BackupMode.CREATE -> {
                    val playlists = channelListRepository.loadChannelsLists()
                        .map { BackupData(name = it.listName) }

                    _viewState.update {
                        it.copy(
                            mode = mode,
                            backupData = playlists
                        )
                    }
                }

                BackupState.BackupMode.RESTORE -> {
                    val backup = backupRepository.getBackup() ?: TvBackup()
                    _viewState.update {
                        it.copy(
                            mode = mode,
                            restoreData = RestoreData(tvBackup = backup)
                        )
                    }
                }
            }
        }
    }
}