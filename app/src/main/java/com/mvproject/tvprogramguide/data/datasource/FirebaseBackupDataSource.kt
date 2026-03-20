package com.mvproject.tvprogramguide.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.mvproject.tvprogramguide.domain.contract.IBackupDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseBackupDataSource(
    private val database: FirebaseDatabase,
    private val databaseTable: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : IBackupDataSource {

    override suspend fun saveBackup(uid: String, tvBackupJson: String) {
        withContext(ioDispatcher) {
            database
                .getReference(databaseTable)
                .child(uid)
                .setValue(tvBackupJson)
                .await()
        }
    }

    override suspend fun getBackup(uid: String): String? =
        withContext(ioDispatcher) {
            try {
                val snapshot = database
                    .getReference(databaseTable)
                    .child(uid)
                    .get()
                    .await()
                snapshot.value?.toString()
            } catch (e: Exception) {
                Timber.e("Error retrieving backup: ${e.message}")
                null
            }
        }
}
