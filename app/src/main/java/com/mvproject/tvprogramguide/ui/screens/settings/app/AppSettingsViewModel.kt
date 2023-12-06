package com.mvproject.tvprogramguide.ui.screens.settings.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.ui.screens.settings.app.action.AppSettingsAction
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
