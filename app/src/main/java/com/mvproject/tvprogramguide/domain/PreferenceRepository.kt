package com.mvproject.tvprogramguide.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.mvproject.tvprogramguide.ui.settings.app.model.AppThemeOptions
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun updateAppTheme(theme: AppThemeOptions) {
        dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = theme.id
        }
    }

    fun loadAppTheme() = dataStore.data.map { preferences ->
        val savedId = preferences[APP_THEME_OPTION] ?: AppThemeOptions.SYSTEM.id
        AppThemeOptions.values().find { theme -> theme.id == savedId } ?: AppThemeOptions.SYSTEM
    }

    private companion object {
        val APP_THEME_OPTION = intPreferencesKey("theme_option")
    }
}