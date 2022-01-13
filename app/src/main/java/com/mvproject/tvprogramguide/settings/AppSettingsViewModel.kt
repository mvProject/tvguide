package com.mvproject.tvprogramguide.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.helpers.StoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val storeHelper: StoreHelper
) : ViewModel() {

    fun setSelectedTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        storeHelper.setSelectedTheme(themeMode)
    }

    fun setSelectedLanguage(language: String){
        storeHelper.setSelectedLanguage(language)
    }

    fun setProgramsCount(count: Int){
        storeHelper.setProgramByChannelDefaultCount(count)
    }

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
}
