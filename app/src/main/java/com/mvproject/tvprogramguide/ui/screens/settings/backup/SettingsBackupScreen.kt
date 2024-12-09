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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.screens.settings.backup.action.BackupAction
import com.mvproject.tvprogramguide.ui.screens.settings.backup.components.GoogleLogoutButton
import com.mvproject.tvprogramguide.ui.screens.settings.backup.components.GoogleSignInButton
import com.mvproject.tvprogramguide.ui.screens.settings.backup.state.BackupState
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.TimeUtils.toFormattedDateTime

@Composable
internal fun SettingsBackupScreen(
    viewModel: SettingsBackupViewModel,
    onNavigateBack: () -> Unit = {}
) {

    val state by viewModel.viewState.collectAsStateWithLifecycle()

// todo display snack after operation complete

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
        Crossfade(
            targetState = state.isUserLogged,
            label = "isUserLogged"
        ) { isLogged ->
            if (isLogged) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(vertical = MaterialTheme.dimens.size8),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
                ) {
                    TabRow(
                        selectedTabIndex = state.mode.ordinal,
                        modifier =
                        Modifier
                            .padding(MaterialTheme.dimens.size4)
                            .clip(MaterialTheme.shapes.medium),
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        indicator = {},
                        divider = {},
                    ) {
                        BackupState.BackupMode.entries.forEachIndexed { index, mode ->
                            val isSelected = state.mode == mode

                            val title = when (mode) {
                                BackupState.BackupMode.CREATE -> R.string.title_backup_mode_create
                                BackupState.BackupMode.RESTORE -> R.string.title_backup_mode_restore
                            }

                            val titleStyle = if (isSelected) {
                                MaterialTheme.typography.titleMedium
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }

                            val selectedTabColor = if (isSelected) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.inverseOnSurface
                            }

                            val selectedTitleColor = if (isSelected) {
                                MaterialTheme.colorScheme.inverseOnSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            Tab(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .drawBehind {
                                        drawRect(color = selectedTabColor)
                                    },
                                selected = isSelected,
                                onClick = { onAction(BackupAction.SetBackupMode(mode)) },
                                text = {
                                    Text(
                                        text = stringResource(id = title),
                                        style = titleStyle,
                                        color = selectedTitleColor
                                    )
                                }
                            )
                        }
                    }

                    Crossfade(targetState = state.mode, label = "backupMode") { mode ->
                        when (mode) {
                            BackupState.BackupMode.CREATE -> {
                                Column(
                                    modifier = Modifier
                                        .weight(MaterialTheme.dimens.weight1)
                                        .padding(horizontal = MaterialTheme.dimens.size8)
                                ) {
                                    state.backupData.forEach { list ->
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = list.name
                                                )
                                            },
                                            trailingContent = {
                                                Checkbox(
                                                    checked = list.isSelected,
                                                    onCheckedChange = {
                                                        onAction(BackupAction.SelectForBackup(list.name))
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkmarkColor = MaterialTheme.colorScheme.inverseOnSurface,
                                                        checkedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                )
                                            },
                                            colors = ListItemDefaults.colors(
                                                containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                            )
                                        )
                                    }
                                }
                            }

                            BackupState.BackupMode.RESTORE -> {
                                Column(
                                    modifier = Modifier
                                        .weight(MaterialTheme.dimens.weight1)
                                        .padding(horizontal = MaterialTheme.dimens.size8)
                                ) {
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = stringResource(R.string.settings_backup_settings_title)
                                            )
                                        },
                                        supportingContent = {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.outline,
                                                text = state.restoreData.tvBackup.timeStamp.toFormattedDateTime()
                                            )
                                        },
                                        trailingContent = {
                                            Checkbox(
                                                checked = state.restoreData.isSelected,
                                                onCheckedChange = {
                                                    onAction(BackupAction.SelectForRestore)
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkmarkColor = MaterialTheme.colorScheme.inverseOnSurface,
                                                    checkedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        },
                                        colors = ListItemDefaults.colors(
                                            containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(MaterialTheme.dimens.weight1))

                    Crossfade(targetState = state.mode, label = "backupMode") { mode ->
                        val title = when (mode) {
                            BackupState.BackupMode.CREATE -> R.string.btn_backup_create
                            BackupState.BackupMode.RESTORE -> R.string.btn_backup_restore
                        }

                        val action = when (mode) {
                            BackupState.BackupMode.CREATE -> BackupAction.CreateBackup
                            BackupState.BackupMode.RESTORE -> BackupAction.RestoreBackup
                        }

                        val isEnabled = when (mode) {
                            BackupState.BackupMode.CREATE -> state.backupData.any { it.isSelected }
                            BackupState.BackupMode.RESTORE -> state.restoreData.isSelected
                        }
                        ElevatedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.size16),
                            enabled = isEnabled,
                            onClick = {
                                onAction(action)
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentColor = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        ) {
                            Text(
                                text = stringResource(id = title),
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    GoogleLogoutButton(onAction = onAction)
                }
            } else {
                Column(
                    modifier =
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(vertical = MaterialTheme.dimens.size8),
                    verticalArrangement = Arrangement.SpaceEvenly,
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

@PreviewLightDark
@Composable
private fun SettingsBackupScreenPreview() {
    TvGuideTheme {
        SettingsBackupScreen(state = BackupState(isUserLogged = true))
    }
}