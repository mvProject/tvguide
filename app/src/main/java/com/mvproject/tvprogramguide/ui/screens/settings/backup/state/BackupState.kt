package com.mvproject.tvprogramguide.ui.screens.settings.backup.state

import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.utils.AppConstants.empty

data class BackupState(
    val isUserLogged: Boolean,
    val mode: BackupMode = BackupMode.CREATE,
    val backupData: List<BackupData> = emptyList(),
    val restoreData: RestoreData = RestoreData()
) {
    enum class BackupMode {
        CREATE,
        RESTORE
    }
}

data class BackupData(
    val name: String = String.empty,
    val isSelected: Boolean = false
)

data class RestoreData(
    val tvBackup: TvBackup = TvBackup(),
    val isSelected: Boolean = false
)