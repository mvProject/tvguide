package com.mvproject.tvprogramguide.domain.contract

interface IBackupDataSource {
    suspend fun saveBackup(uid: String, tvBackupJson: String)
    suspend fun getBackup(uid: String): String?
}
