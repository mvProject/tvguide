package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvproject.tvprogramguide.MainActivity
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.settings.*
import com.mvproject.tvprogramguide.utils.Utils.findActivity

@Composable
fun SettingsContent(settingsViewModel: AppSettingsViewModel = viewModel()) {
    val selectedTheme = settingsViewModel.selectedThemeMode
    val selectedLanguage = settingsViewModel.selectedLanguage

    val themeOptionsStrings = settingsViewModel
        .themeOptions
        .map { stringResource(id = it.getProperTitle()) }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        settingsViewModel.uiEvent.collect { state ->
            when (state) {
                is AppSettingState.UpdateUI -> {
                    (context.findActivity() as? MainActivity)?.recreate()
                }
            }
        }
    }
    Column {
        PickerItem(
            title = stringResource(id = R.string.settings_channels_update_title),
            min = 1,
            max = 7,
            initialValue = settingsViewModel.updateChannelsPeriod
        ) {
            settingsViewModel.onEvent(AppSettingEvent.OnChannelUpdatePeriodChange(it))
        }

        PickerItem(
            title = stringResource(id = R.string.settings_programs_update_title),
            min = 1,
            max = 7,
            initialValue = settingsViewModel.updateProgramsPeriod
        ) {
            settingsViewModel.onEvent(AppSettingEvent.OnProgramUpdatePeriodChange(it))
        }

        PickerItem(
            title = stringResource(id = R.string.settings_programs_count_title),
            min = 2,
            max = 6,
            initialValue = settingsViewModel.programsViewCount
        ) {
            settingsViewModel.onEvent(AppSettingEvent.OnProgramVisibleCountChange(it))
        }

        RadioGroup(
            radioOptions = settingsViewModel.languageOptionsNames,
            title = stringResource(id = R.string.settings_language_title),
            cardBackgroundColor = Color(0xFFFFFAF0),
            defaultSelection = settingsViewModel.getLanguageDefaultSelectedIndex(selectedLanguage)
        ) { selectedLanguage ->
            settingsViewModel.onEvent(AppSettingEvent.OnLanguageChange(selectedLanguage))
        }

        RadioGroup(
            radioOptions = themeOptionsStrings,
            title = stringResource(id = R.string.settings_theme_title),
            cardBackgroundColor = Color(0xFFF8F8FF),
            defaultSelection = settingsViewModel.getThemeDefaultSelectedIndex(selectedTheme)
        ) { selectedTheme ->
            val selected = themeOptionsStrings
                .indexOfFirst { it == selectedTheme }
            settingsViewModel.onEvent(AppSettingEvent.OnThemeChange(selected))
        }
    }
}

