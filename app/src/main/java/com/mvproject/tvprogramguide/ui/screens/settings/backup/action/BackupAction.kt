package com.mvproject.tvprogramguide.ui.screens.settings.backup.action

import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupState

sealed interface BackupAction {
    data object ProfileLogin : BackupAction
    data object ProfileLogout : BackupAction
    data class SetBackupMode(val mode: BackupState.BackupMode) : BackupAction
    data class SelectForBackup(val name: String) : BackupAction
    data object SelectForRestore : BackupAction
    data object CreateBackup : BackupAction
    data object RestoreBackup : BackupAction
}