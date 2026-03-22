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
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mvproject.tvprogramguide.feature.settings.R
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun GoogleLogoutButton(onAction: (BackupAction) -> Unit) {
    val auth = Firebase.auth
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.size16),
        onClick = {
            scope.launch {
                auth.signOut()
                credentialManager.clearCredentialState(
                    ClearCredentialStateRequest()
                )
            }
            onAction(BackupAction.ProfileLogout)
        }
    ) {
        Text(
            text = stringResource(id = R.string.btn_profile_logout),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}