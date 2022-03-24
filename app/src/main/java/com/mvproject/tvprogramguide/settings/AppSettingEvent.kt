package com.mvproject.tvprogramguide.settings

sealed class AppSettingEvent {
    data class OnChannelUpdatePeriodChange(val period: Int) : AppSettingEvent()
    data class OnProgramUpdatePeriodChange(val period: Int) : AppSettingEvent()
    data class OnProgramVisibleCountChange(val count: Int) : AppSettingEvent()
    data class OnLanguageChange(val selectedLanguage: String) : AppSettingEvent()
    data class OnThemeChange(val selectedTheme: Int) : AppSettingEvent()
}