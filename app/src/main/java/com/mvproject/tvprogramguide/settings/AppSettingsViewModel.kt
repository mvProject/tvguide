package com.mvproject.tvprogramguide.settings

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

    private val _uiEvent = Channel<AppSettingState>()
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

    fun onEvent(event: AppSettingEvent) {
        when (event) {
            is AppSettingEvent.OnChannelUpdatePeriodChange -> {
                storeHelper.setChannelsUpdatePeriod(event.period)
            }
            is AppSettingEvent.OnProgramUpdatePeriodChange -> {
                storeHelper.setProgramsUpdatePeriod(event.period)
            }
            is AppSettingEvent.OnProgramVisibleCountChange -> {
                storeHelper.setProgramByChannelDefaultCount(event.count)
            }
            is AppSettingEvent.OnLanguageChange -> {
                setSelectedLanguage(event.selectedLanguage)
                sendEvent(AppSettingState.UpdateUI)
            }
            is AppSettingEvent.OnThemeChange -> {
                setSelectedTheme(event.selectedTheme)
                sendEvent(AppSettingState.UpdateUI)
            }
        }
    }

    private fun sendEvent(event: AppSettingState) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
