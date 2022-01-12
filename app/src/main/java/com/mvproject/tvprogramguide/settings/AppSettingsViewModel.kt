package com.mvproject.tvprogramguide.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.Utils.toThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val storeHelper: StoreHelper
) : ViewModel() {

    fun setSelectedTheme(themeSelected: String) {
        AppCompatDelegate.setDefaultNightMode(themeSelected.toThemeMode())
        storeHelper.setSelectedTheme(themeSelected.toThemeMode())
    }

    fun setSelectedLanguage(language: String){
        storeHelper.setSelectedLanguage(language)
    }

    val selectedThemeMode
        get() = storeHelper.selectedTheme

    val selectedLanguage
        get() = storeHelper.selectedLang
}
