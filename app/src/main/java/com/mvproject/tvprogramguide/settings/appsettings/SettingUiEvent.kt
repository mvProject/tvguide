package com.mvproject.tvprogramguide.settings.appsettings

sealed class SettingUiEvent {
    object UpdateUI : SettingUiEvent()
}