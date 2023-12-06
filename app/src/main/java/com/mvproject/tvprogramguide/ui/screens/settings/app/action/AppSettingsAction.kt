package com.mvproject.tvprogramguide.ui.screens.settings.app.action

sealed class AppSettingsAction {
    data class ChannelUpdatePeriodChange(val period: Int) : AppSettingsAction()
    data class ProgramUpdatePeriodChange(val period: Int) : AppSettingsAction()
    data class ProgramVisibleCountChange(val count: Int) : AppSettingsAction()
    data class ThemeChange(val selectedTheme: Int) : AppSettingsAction()
}
