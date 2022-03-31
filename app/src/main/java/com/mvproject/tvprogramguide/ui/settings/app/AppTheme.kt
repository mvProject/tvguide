package com.mvproject.tvprogramguide.ui.settings.app

import androidx.appcompat.app.AppCompatDelegate
import com.mvproject.tvprogramguide.R

sealed class AppTheme(val value: Int) {
    class Light : AppTheme(AppCompatDelegate.MODE_NIGHT_NO) {
        override fun getProperTitle() = R.string.settings_theme_light
    }

    class Dark : AppTheme(AppCompatDelegate.MODE_NIGHT_YES) {
        override fun getProperTitle() = R.string.settings_theme_dark
    }

    class System : AppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
        override fun getProperTitle() = R.string.settings_theme_system
    }

    abstract fun getProperTitle(): Int
}