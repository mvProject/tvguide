package com.mvproject.tvprogramguide.settings.appsettings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.MainActivity
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.PickerItem
import com.mvproject.tvprogramguide.components.RadioGroup
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.utils.Utils.findActivity

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SettingsContent() {

    val settingsViewModel: AppSettingsViewModel = hiltViewModel()

    val selectedTheme = settingsViewModel.selectedThemeMode
    val selectedLanguage = settingsViewModel.selectedLanguage

    val themeOptionsStrings = settingsViewModel
        .themeOptions
        .map { stringResource(id = it.getProperTitle()) }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        settingsViewModel.uiEvent.collect { state ->
            when (state) {
                is SettingUiEvent.UpdateUI -> {
                    (context.findActivity() as? MainActivity)?.recreate()
                }
            }
        }
    }
    Column {
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
                    initialValue = settingsViewModel.updateChannelsPeriod
                ) {
                    settingsViewModel.onEvent(SettingAction.ChannelUpdatePeriodChange(it))
                }

                PickerItem(
                    title = stringResource(id = R.string.settings_programs_update_title),
                    min = 1,
                    max = 7,
                    initialValue = settingsViewModel.updateProgramsPeriod
                ) {
                    settingsViewModel.onEvent(SettingAction.ProgramUpdatePeriodChange(it))
                }

                PickerItem(
                    title = stringResource(id = R.string.settings_programs_count_title),
                    min = 2,
                    max = 6,
                    initialValue = settingsViewModel.programsViewCount
                ) {
                    settingsViewModel.onEvent(SettingAction.ProgramVisibleCountChange(it))
                }
            }
        }

        RadioGroup(
            radioOptions = settingsViewModel.languageOptionsNames,
            title = stringResource(id = R.string.settings_language_title),
            cardBackgroundColor = MaterialTheme.appColors.backgroundPrimary,
            defaultSelection = settingsViewModel.getLanguageDefaultSelectedIndex(selectedLanguage)
        ) { selectedLanguage ->
            settingsViewModel.onEvent(SettingAction.LanguageChange(selectedLanguage))
        }

        RadioGroup(
            radioOptions = themeOptionsStrings,
            title = stringResource(id = R.string.settings_theme_title),
            cardBackgroundColor = MaterialTheme.appColors.backgroundPrimary,
            defaultSelection = settingsViewModel.getThemeDefaultSelectedIndex(selectedTheme)
        ) { selectedTheme ->
            val selected = themeOptionsStrings
                .indexOfFirst { it == selectedTheme }
            settingsViewModel.onEvent(SettingAction.ThemeChange(selected))
        }
    }
}

