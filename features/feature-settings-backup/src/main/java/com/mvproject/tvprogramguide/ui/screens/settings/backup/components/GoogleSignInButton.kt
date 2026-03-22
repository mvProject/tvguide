package com.mvproject.tvprogramguide.ui.screens.settings.backup.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
// BuildConfig removed - webClientId passed as parameter
import com.mvproject.tvprogramguide.feature.settings.backup.R
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.theme.dimens
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun GoogleSignInButton(webClientId: String, onAction: (BackupAction) -> Unit) {
    val auth = Firebase.auth
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.size16),
        onClick = {
            val googleOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = context,
                        request = request
                    )
                    val credential = result.credential
                    val tokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val token = tokenCredential.idToken
                    val firebaseCredential =
                        GoogleAuthProvider.getCredential(token, null)

                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onAction(BackupAction.ProfileLogin)
                            }
                        }
                        .addOnFailureListener { failure ->
                            Timber.e("testing login error ${failure.message}")
                            onAction(BackupAction.ProfileLogout)
                        }
                } catch (e: Throwable) {
                    Timber.e("testing login throwable ${e.message}")
                    onAction(BackupAction.ProfileLogout)
                }
            }
        }
    ) {
        Text(
            text = stringResource(id = R.string.btn_profile_login),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}
