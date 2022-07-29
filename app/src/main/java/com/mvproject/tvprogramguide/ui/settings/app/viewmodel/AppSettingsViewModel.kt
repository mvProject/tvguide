package com.mvproject.tvprogramguide.ui.settings.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.ui.settings.app.action.SettingAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private var _settingsState = MutableStateFlow(AppSettingsModel())
    val settingsState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.loadAppSettings().collect { settings ->
                _settingsState.value = settings
            }
        }
    }

    val getThemeDefaultSelectedIndex
        get() = AppThemeOptions.values().indexOfFirst { it.id == settingsState.value.appTheme }

    fun processAction(action: SettingAction) {
        when (action) {
            is SettingAction.ChannelUpdatePeriodChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(channelsUpdatePeriod = action.period)
                    )
                }
            }
            is SettingAction.ProgramUpdatePeriodChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(programsUpdatePeriod = action.period)
                    )
                }
            }
            is SettingAction.ProgramVisibleCountChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(programsViewCount = action.count)
                    )
                }
            }
            is SettingAction.ThemeChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(appTheme = action.selectedTheme)
                    )
                }
            }
        }
    }
}
