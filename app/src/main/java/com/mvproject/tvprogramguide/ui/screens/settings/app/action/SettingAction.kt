package com.mvproject.tvprogramguide.ui.screens.settings.app.action

sealed class SettingAction {
    data class ChannelUpdatePeriodChange(val period: Int) : SettingAction()
    data class ProgramUpdatePeriodChange(val period: Int) : SettingAction()
    data class ProgramVisibleCountChange(val count: Int) : SettingAction()
    data class ThemeChange(val selectedTheme: Int) : SettingAction()
}
