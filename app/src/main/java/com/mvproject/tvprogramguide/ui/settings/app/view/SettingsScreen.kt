package com.mvproject.tvprogramguide.ui.settings.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.radio.RadioGroup
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.settings.app.action.SettingAction
import com.mvproject.tvprogramguide.ui.settings.app.components.PickerItem
import com.mvproject.tvprogramguide.ui.settings.app.viewmodel.AppSettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: AppSettingsViewModel,
    onBackClick: () -> Unit
) {
    val themeOptionsStrings = viewModel
        .themeOptions
        .map { stringResource(id = it.titleRes) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
            onBackClick()
        }

        Card(
            backgroundColor = MaterialTheme.appColors.backgroundPrimary,
            modifier = Modifier
                .padding(MaterialTheme.dimens.size8)
                .fillMaxWidth(),
            elevation = MaterialTheme.dimens.size8,
            shape = RoundedCornerShape(MaterialTheme.dimens.size8)
        ) {
            Column {
                PickerItem(
                    title = stringResource(id = R.string.settings_channels_update_title),
                    min = 1,
                    max = 7,
                    initialValue = viewModel.updateChannelsPeriod
                ) {
                    viewModel.processAction(SettingAction.ChannelUpdatePeriodChange(it))
                }

                PickerItem(
                    title = stringResource(id = R.string.settings_programs_update_title),
                    min = 1,
                    max = 7,
                    initialValue = viewModel.updateProgramsPeriod
                ) {
                    viewModel.processAction(SettingAction.ProgramUpdatePeriodChange(it))
                }

                PickerItem(
                    title = stringResource(id = R.string.settings_programs_count_title),
                    min = 2,
                    max = 6,
                    initialValue = viewModel.programsViewCount
                ) {
                    viewModel.processAction(SettingAction.ProgramVisibleCountChange(it))
                }
            }
        }

        RadioGroup(
            radioOptions = themeOptionsStrings,
            title = stringResource(id = R.string.settings_theme_title),
            cardBackgroundColor = MaterialTheme.appColors.backgroundPrimary,
            defaultSelection = viewModel.getThemeDefaultSelectedIndex
        ) { selectedTheme ->
            val selected = themeOptionsStrings
                .indexOfFirst { it == selectedTheme }
            viewModel.processAction(SettingAction.ThemeChange(selected))
        }
    }

}