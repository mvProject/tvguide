package com.mvproject.tvprogramguide.ui.settings.app

sealed class AppLang(val name: String, val locale: String) {
    object English : AppLang("English", "en")
    object Russian : AppLang("Русский", "ru")
    object Ukrainian : AppLang("Українська", "uk")
}
