package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.backup.TvBackup

interface IBackupRepository {
    suspend fun saveBackup(tvBackup: TvBackup)
    suspend fun getBackup(): TvBackup?
}
