package com.mvproject.tvprogramguide.ui.screens.settings.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.ui.screens.settings.app.action.AppSettingsAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val settingsState by lazy {
        preferenceRepository.loadAppSettings()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(3000L),
                AppSettingsModel()
            )
    }

    val getThemeDefaultSelectedIndex
        get() = AppThemeOptions.entries.indexOfFirst { it.id == settingsState.value.appTheme }

    fun processAction(action: AppSettingsAction) {
        when (action) {
            is AppSettingsAction.ChannelUpdatePeriodChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(channelsUpdatePeriod = action.period)
                    )
                }
            }

            is AppSettingsAction.ProgramUpdatePeriodChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(programsUpdatePeriod = action.period)
                    )
                }
            }

            is AppSettingsAction.ProgramVisibleCountChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(programsViewCount = action.count)
                    )
                }
            }

            is AppSettingsAction.ThemeChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    preferenceRepository.setAppSettings(
                        appSettings = settingsState.value.copy(appTheme = action.selectedTheme)
                    )
                }
            }
        }
    }
}
