package com.mvproject.tvprogramguide.ui.settings.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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

    init {
        Timber.i("testing AppSettingsViewModel init")
    }

    fun processAction(action: SettingAction) {
        when (action) {
            is SettingAction.ChannelUpdatePeriodChange -> {
                storeHelper.setChannelsUpdatePeriod(action.period)
            }
            is SettingAction.ProgramUpdatePeriodChange -> {
                storeHelper.setProgramsUpdatePeriod(action.period)
            }
            is SettingAction.ProgramVisibleCountChange -> {
                storeHelper.setProgramByChannelDefaultCount(action.count)
            }
            is SettingAction.LanguageChange -> {
                setSelectedLanguage(action.selectedLanguage)
                sendEvent(SettingUiEvent.UpdateUI)
            }
            is SettingAction.ThemeChange -> {
                setSelectedTheme(action.selectedTheme)
                sendEvent(SettingUiEvent.UpdateUI)
            }
        }
    }

    private fun sendEvent(event: SettingUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("testing AppSettingsViewModel onCleared")
    }
}
