package com.mvproject.tvprogramguide.ui.screens.settings.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.ui.components.pickers.PickerItem
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroup
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.screens.settings.app.action.AppSettingsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun AppSettingsScreen(
    viewModel: AppSettingsViewModel,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.settings_title),
                onBackClick = onNavigateBack,
            )
        },
    ) { contentPadding ->
        Column(
            modifier =
                Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
        ) {
            val settings by viewModel.settingsState.collectAsState()

            ListItem(
                colors =
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.settings_ui_title),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
            )

            PickerItem(
                title = stringResource(id = R.string.settings_programs_count_title),
                min = 2,
                max = 6,
                initialValue = settings.programsViewCount,
            ) {
                viewModel.processAction(AppSettingsAction.ProgramVisibleCountChange(it))
            }

            ListItem(
                colors =
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.settings_updates_title),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
            )

            PickerItem(
                title = stringResource(id = R.string.settings_channels_update_title),
                min = 1,
                max = 7,
                initialValue = settings.channelsUpdatePeriod,
            ) {
                viewModel.processAction(AppSettingsAction.ChannelUpdatePeriodChange(it))
            }

            PickerItem(
                title = stringResource(id = R.string.settings_programs_update_title),
                min = 1,
                max = 7,
                initialValue = settings.programsUpdatePeriod,
            ) {
                viewModel.processAction(AppSettingsAction.ProgramUpdatePeriodChange(it))
            }

            val themeOptionsStrings =
                AppThemeOptions.entries.map { stringResource(id = it.titleRes) }

            ListItem(
                colors =
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.settings_theme_title),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
            )

            RadioGroup(
                radioOptions = themeOptionsStrings,
                defaultSelection = viewModel.getThemeDefaultSelectedIndex,
            ) { selectedTheme ->
                val selected =
                    themeOptionsStrings
                        .indexOfFirst { it == selectedTheme }
                viewModel.processAction(AppSettingsAction.ThemeChange(selected))
            }
        }
    }
}
