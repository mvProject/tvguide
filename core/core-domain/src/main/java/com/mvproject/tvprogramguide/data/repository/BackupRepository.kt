package com.mvproject.tvprogramguide.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.domain.contract.IBackupDataSource
import com.mvproject.tvprogramguide.domain.contract.IBackupRepository
import kotlinx.serialization.json.Json
import timber.log.Timber

class BackupRepository(
    private val backupDataSource: IBackupDataSource,
    private val auth: FirebaseAuth,
) : IBackupRepository {

    override suspend fun saveBackup(tvBackup: TvBackup) {
        auth.currentUser?.let { user ->
            val tvBackupJson = Json.encodeToString(tvBackup)
            backupDataSource.saveBackup(uid = user.uid, tvBackupJson = tvBackupJson)
        }
    }

    override suspend fun getBackup(): TvBackup? {
        return auth.currentUser?.let { user ->
            val json = backupDataSource.getBackup(uid = user.uid) ?: return null
            try {
                Json.decodeFromString<TvBackup>(json)
            } catch (e: Exception) {
                Timber.e("Backup decode error: ${e.message}")
                null
            }
        }
    }
}
