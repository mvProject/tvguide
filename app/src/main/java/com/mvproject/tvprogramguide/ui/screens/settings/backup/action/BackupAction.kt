package com.mvproject.tvprogramguide.ui.screens.settings.backup.action

sealed interface BackupAction {
    data object ProfileLogin : BackupAction
    data object ProfileLogout : BackupAction
}