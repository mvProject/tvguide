package com.mvproject.tvprogramguide.ui.settings.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.theme.appColors
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
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
            onBackClick()
        }

        val settings by viewModel.settingsState.collectAsState()

        Card(
            backgroundColor = MaterialTheme.appColors.backgroundPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8),
            elevation = MaterialTheme.dimens.size8,
            shape = RoundedCornerShape(MaterialTheme.dimens.size8)
        ) {
            Column {
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

                PickerItem(
                    title = stringResource(id = R.string.settings_programs_count_title),
                    min = 2,
                    max = 6,
                    initialValue = settings.programsViewCount
                ) {
                    viewModel.processAction(SettingAction.ProgramVisibleCountChange(it))
                }
            }
        }

        val themeOptionsStrings =
            AppThemeOptions.values().map { stringResource(id = it.titleRes) }

        RadioGroup(
            radioOptions = themeOptionsStrings,
            title = stringResource(id = R.string.settings_theme_title),
            defaultSelection = viewModel.getThemeDefaultSelectedIndex
        ) { selectedTheme ->
            val selected = themeOptionsStrings
                .indexOfFirst { it == selectedTheme }
            viewModel.processAction(SettingAction.ThemeChange(selected))
        }
    }

}