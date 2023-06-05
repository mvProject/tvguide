package com.mvproject.tvprogramguide.ui.settings.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroup
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.settings.app.action.SettingAction
import com.mvproject.tvprogramguide.ui.settings.app.components.PickerItem
import com.mvproject.tvprogramguide.ui.settings.app.viewmodel.AppSettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: AppSettingsViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
                onBackClick()
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            val settings by viewModel.settingsState.collectAsState()

            Card(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.size6)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = stringResource(id = R.string.settings_ui_title),
                    modifier = Modifier
                        .padding(all = MaterialTheme.dimens.size6),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            PickerItem(
                title = stringResource(id = R.string.settings_programs_count_title),
                min = 2,
                max = 6,
                initialValue = settings.programsViewCount
            ) {
                viewModel.processAction(SettingAction.ProgramVisibleCountChange(it))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            Card(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.size6)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = stringResource(id = R.string.settings_updates_title),
                    modifier = Modifier
                        .padding(all = MaterialTheme.dimens.size6),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            PickerItem(
                title = stringResource(id = R.string.settings_channels_update_title),
                min = 1,
                max = 7,
                initialValue = settings.channelsUpdatePeriod
            ) {
                viewModel.processAction(SettingAction.ChannelUpdatePeriodChange(it))
            }

            PickerItem(
                title = stringResource(id = R.string.settings_programs_update_title),
                min = 1,
                max = 7,
                initialValue = settings.programsUpdatePeriod
            ) {
                viewModel.processAction(SettingAction.ProgramUpdatePeriodChange(it))
            }

            val themeOptionsStrings =
                AppThemeOptions.values().map { stringResource(id = it.titleRes) }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Card(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.size6)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = stringResource(id = R.string.settings_theme_title),
                    modifier = Modifier
                        .padding(all = MaterialTheme.dimens.size6),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            RadioGroup(
                radioOptions = themeOptionsStrings,
                defaultSelection = viewModel.getThemeDefaultSelectedIndex
            ) { selectedTheme ->
                val selected = themeOptionsStrings
                    .indexOfFirst { it == selectedTheme }
                viewModel.processAction(SettingAction.ThemeChange(selected))
            }
        }
    }
}
