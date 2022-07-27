package com.mvproject.tvprogramguide.ui.settings.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.domain.PreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import com.mvproject.tvprogramguide.ui.settings.app.action.SettingAction
import com.mvproject.tvprogramguide.ui.settings.app.model.AppThemeOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private var themeState = mutableStateOf(AppThemeOptions.SYSTEM.id)

    val themeOptions = AppThemeOptions.values()

    init {
        viewModelScope.launch {
            preferenceRepository.loadAppTheme().collect {
                themeState.value = it.id
            }
        }
    }

    val updateChannelsPeriod
        get() = storeHelper.channelsUpdatePeriod

    val updateProgramsPeriod
        get() = storeHelper.programsUpdatePeriod

    val programsViewCount
        get() = storeHelper.programByChannelDefaultCount

    val getThemeDefaultSelectedIndex
        get() = themeOptions.indexOfFirst { it.id == themeState.value }

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
            is SettingAction.ThemeChange -> {
                val themeMode = themeOptions[action.selectedTheme]
                viewModelScope.launch {
                    preferenceRepository.updateAppTheme(themeMode)
                }
            }
        }
    }
}
