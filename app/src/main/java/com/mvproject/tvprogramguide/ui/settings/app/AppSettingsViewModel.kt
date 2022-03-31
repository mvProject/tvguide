package com.mvproject.tvprogramguide.ui.settings.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.helpers.StoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val storeHelper: StoreHelper
) : ViewModel() {

    private val _uiEvent = Channel<SettingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val selectedThemeMode
        get() = storeHelper.selectedTheme

    val selectedLanguage
        get() = storeHelper.selectedLang

    val updateChannelsPeriod
        get() = storeHelper.channelsUpdatePeriod

    val updateProgramsPeriod
        get() = storeHelper.programsUpdatePeriod

    val programsViewCount
        get() = storeHelper.programByChannelDefaultCount

    private val languageOptions = listOf(AppLang.English, AppLang.Russian, AppLang.Ukrainian)
    val languageOptionsNames = languageOptions.map { it.name }

    fun getLanguageDefaultSelectedIndex(selected: String) =
        languageOptions.indexOfFirst { it.locale == selected }

    val themeOptions = listOf(AppTheme.Light(), AppTheme.Dark(), AppTheme.System())

    fun getThemeDefaultSelectedIndex(selected: Int) =
        themeOptions.indexOfFirst { it.value == selected }

    private fun setSelectedTheme(selected: Int) {
        val themeMode = themeOptions[selected].value
        AppCompatDelegate.setDefaultNightMode(themeMode)
        storeHelper.setSelectedTheme(themeMode)
    }

    private fun setSelectedLanguage(language: String) {
        languageOptions.firstOrNull { it.name == language }?.locale?.let { locale ->
            storeHelper.setSelectedLanguage(locale)
        }
    }

    fun onEvent(event: SettingAction) {
        when (event) {
            is SettingAction.ChannelUpdatePeriodChange -> {
                storeHelper.setChannelsUpdatePeriod(event.period)
            }
            is SettingAction.ProgramUpdatePeriodChange -> {
                storeHelper.setProgramsUpdatePeriod(event.period)
            }
            is SettingAction.ProgramVisibleCountChange -> {
                storeHelper.setProgramByChannelDefaultCount(event.count)
            }
            is SettingAction.LanguageChange -> {
                setSelectedLanguage(event.selectedLanguage)
                sendEvent(SettingUiEvent.UpdateUI)
            }
            is SettingAction.ThemeChange -> {
                setSelectedTheme(event.selectedTheme)
                sendEvent(SettingUiEvent.UpdateUI)
            }
        }
    }

    private fun sendEvent(event: SettingUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
