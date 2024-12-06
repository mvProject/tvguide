package com.mvproject.tvprogramguide.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mvproject.tvprogramguide.BuildConfig
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Repository class responsible for managing Firebase-based backup operations.
 * Handles saving and retrieving user data backups using Firebase Realtime Database.
 * The backup data is stored per user, identified by their email address.
 */
class BackupRepository {
    private val database = Firebase.database(BuildConfig.FIREBASE_DATABASE_URL)

    private val auth = Firebase.auth

    /**
     * Saves the provided backup data to Firebase for the currently authenticated user.
     * The backup is stored in JSON format under the user's email-based key.
     *
     * @param tvBackup The backup data to be saved
     */
    suspend fun saveBackup(tvBackup: TvBackup) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                val email = emailToKey(user)
                if (email.isNotBlank()) {

                    val tvBackupJson = Json.encodeToString(tvBackup)

                    database.getReference(BuildConfig.FIREBASE_DATABASE_TABLE)
                        .child(email)
                        .setValue(tvBackupJson)
                }
            }
        }
    }

    /**
     * Retrieves the backup data for the current user from Firebase Realtime Database.
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

                    val email = emailToKey(user)

                    if (email.isNotBlank()) {
                        val dataSnapshot = database
                            .getReference(BuildConfig.FIREBASE_DATABASE_TABLE)
                            .child(email)
                            .get()
                            .await()

                        val tvBackup = dataSnapshot.value?.let {
                            Json.decodeFromString<TvBackup>(it.toString())
                        }
                        tvBackup
                    } else null
                }
            } catch (e: Exception) {
                Timber.e("testing Error retrieving backup")
                null
            }
        }

    /**
     * Transforms a user's email address to a valid Firebase database key.
     * Firebase doesn't allow periods in keys, so they are replaced with '@'.
     *
     * @param user The Firebase user whose email needs to be transformed
     * @return The transformed email address or empty string if no email exists
     */
    private fun emailToKey(user: FirebaseUser): String {
        return user.email?.replace(".", "@") ?: String.empty
    }
}