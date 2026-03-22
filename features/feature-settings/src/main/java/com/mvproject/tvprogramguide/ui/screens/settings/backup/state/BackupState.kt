package com.mvproject.tvprogramguide.ui.screens.settings.backup.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class BackupState(
    val isUserLogged: Boolean,
    val mode: BackupMode = BackupMode.CREATE,
    val backupData: ImmutableList<BackupData> = persistentListOf(),
    val restoreData: RestoreData = RestoreData()
) {
    enum class BackupMode {
        CREATE,
        RESTORE
    }
}

@Immutable
data class BackupData(
    val name: String = String.empty,
    val isSelected: Boolean = false
)

@Immutable
data class RestoreData(
    val tvBackup: TvBackup = TvBackup(),
    val isSelected: Boolean = false
)