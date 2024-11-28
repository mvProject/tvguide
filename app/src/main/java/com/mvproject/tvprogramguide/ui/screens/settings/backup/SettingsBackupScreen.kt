package com.mvproject.tvprogramguide.ui.screens.settings.backup

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.SettingsMenu
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.screens.settings.backup.components.GoogleLogoutButton
import com.mvproject.tvprogramguide.ui.screens.settings.backup.components.GoogleSignInButton
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupState
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
internal fun SettingsBackupScreen(
    viewModel: SettingsBackupViewModel,
    onNavigateBack: () -> Unit = {}
) {

    val state by viewModel.viewState.collectAsStateWithLifecycle()

    SettingsBackupScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )

}

@Composable
private fun SettingsBackupScreen(
    state: BackupState,
    onAction: (BackupAction) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.settings_backup_settings_title),
                onBackClick = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(vertical = MaterialTheme.dimens.size8),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
        ) {

            Crossfade(targetState = state.isUserLogged, label = "isUserLogged") { isLogged ->
                if (isLogged) {
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size8)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SettingsMenu(
                            title = stringResource(id = R.string.btn_backup_create),
                            onAction = {

                            },
                        )

                        SettingsMenu(
                            title = stringResource(id = R.string.btn_backup_restore),
                            onAction = {

                            },
                        )

                        Spacer(modifier = Modifier.weight(MaterialTheme.dimens.weight1))

                        GoogleLogoutButton(onAction = onAction)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size8)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimens.size16)
                                .fillMaxWidth()
                                .border(
                                    width = MaterialTheme.dimens.size1,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(MaterialTheme.dimens.size34)
                        ) {
                            Text(
                                text = stringResource(id = R.string.btn_profile_login_requirement),
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        GoogleSignInButton(onAction = onAction)
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SettingsBackupScreenPreview() {
    TvGuideTheme {
        SettingsBackupScreen(state = BackupState(isUserLogged = true))
    }
}
