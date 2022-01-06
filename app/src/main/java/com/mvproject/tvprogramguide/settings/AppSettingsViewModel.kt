package com.mvproject.tvprogramguide.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.utils.Utils.toThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val storeManager: StoreManager
) : ViewModel() {

    fun setSelectedTheme(themeSelected: String) {
        AppCompatDelegate.setDefaultNightMode(themeSelected.toThemeMode())
        storeManager.setSelectedTheme(themeSelected.toThemeMode())
    }

    fun setSelectedLanguage(language: String){
        storeManager.setSelectedLanguage(language)
    }

    val selectedThemeMode
        get() = storeManager.selectedTheme

    val selectedLanguage
        get() = storeManager.selectedLang
}
