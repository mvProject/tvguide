package com.mvproject.tvprogramguide.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.mvproject.tvprogramguide.BuildConfig
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Repository class responsible for managing Firebase-based backup operations.
 * Handles saving and retrieving user data backups using Firebase Realtime Database.
 * The backup data is stored per user, identified by their unique Firebase UID.
 *
 * @property database Firebase Realtime Database instance configured with the application's database URL
 * @property auth Firebase Authentication instance for user verification
 */
class BackupRepository {
    private val database = Firebase.database(BuildConfig.FIREBASE_DATABASE_URL)

    private val auth = Firebase.auth

    /**
     * Saves the provided backup data to Firebase for the currently authenticated user.
     * The backup is stored in JSON format under the user's unique Firebase UID.
     * This operation is performed on IO dispatcher to prevent blocking the main thread.
     *
     * @param tvBackup The backup data to be saved in JSON format
     */
    suspend fun saveBackup(tvBackup: TvBackup) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->

                val tvBackupJson = Json.encodeToString(tvBackup)

                database.getReference(BuildConfig.FIREBASE_DATABASE_TABLE)
                    .child(user.uid)
                    .setValue(tvBackupJson)
            }
        }
    }

    /**
     * Retrieves the backup data for the current user from Firebase Realtime Database.
     * The backup is fetched using the user's unique Firebase UID.
     * This operation is performed on IO dispatcher to prevent blocking the main thread.
     *
     * @return The retrieved [TvBackup] object or null if:
     * - No user is logged in
     * - No backup exists for the user
     * - An error occurs during retrieval
     */
    suspend fun getBackup() =
        withContext(Dispatchers.IO) {
            try {
                auth.currentUser?.let { user ->
                    val dataSnapshot = database
                        .getReference(BuildConfig.FIREBASE_DATABASE_TABLE)
                        .child(user.uid)
                        .get()
                        .await()

                    val tvBackup = dataSnapshot.value?.let {
                        Json.decodeFromString<TvBackup>(it.toString())
                    }
                    tvBackup
                }
            } catch (e: Exception) {
                Timber.e("testing Error retrieving backup")
                null
            }
        }
}