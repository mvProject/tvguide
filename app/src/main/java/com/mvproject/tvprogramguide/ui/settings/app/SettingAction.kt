package com.mvproject.tvprogramguide.ui.settings.app

sealed class SettingAction {
    data class ChannelUpdatePeriodChange(val period: Int) : SettingAction()
    data class ProgramUpdatePeriodChange(val period: Int) : SettingAction()
    data class ProgramVisibleCountChange(val count: Int) : SettingAction()
    data class LanguageChange(val selectedLanguage: String) : SettingAction()
    data class ThemeChange(val selectedTheme: Int) : SettingAction()
}