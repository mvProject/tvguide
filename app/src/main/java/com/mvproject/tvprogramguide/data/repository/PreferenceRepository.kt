package com.mvproject.tvprogramguide.data.repository

import android.text.format.DateUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_LONG
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setCurrentUserList(listName: String) {
        dataStore.edit { settings ->
            settings[CURRENT_CHANNEL_LIST] = listName
        }
    }

    private fun loadCurrentUserList() = dataStore.data.map { preferences ->
        preferences[CURRENT_CHANNEL_LIST] ?: NO_VALUE_STRING
    }

    suspend fun setDefaultUserList(listName: String) {
        dataStore.edit { settings ->
            settings[DEFAULT_CHANNEL_LIST] = listName
        }
    }

    fun loadDefaultUserList() = dataStore.data.map { preferences ->
        preferences[DEFAULT_CHANNEL_LIST] ?: NO_VALUE_STRING
    }

    val targetList = combine(
        loadCurrentUserList(),
        loadDefaultUserList()
    ) { current, default ->
        return@combine current.ifEmpty { default }
    }

    val isNeedFullProgramsUpdate = combine(
        loadDefaultUserList(),
        loadAppSettings(),
        loadProgramsUpdateLastTime()
    ) { userList, settings, lastUpdate ->
        return@combine userList.isNotEmpty() &&
                System.currentTimeMillis() - lastUpdate >
                DateUtils.DAY_IN_MILLIS * settings.programsUpdatePeriod
    }

    val isNeedAvailableChannelsUpdate = combine(
        loadAppSettings(),
        loadChannelsUpdateLastTime()
    ) { settings, lastUpdate ->
        return@combine System.currentTimeMillis() - lastUpdate >
                DateUtils.DAY_IN_MILLIS * settings.channelsUpdatePeriod
    }

    suspend fun setChannelsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_CHANNELS] = timeInMillis
        }
    }

    private fun loadChannelsUpdateLastTime() = dataStore.data.map { preferences ->
        preferences[LAST_UPDATE_CHANNELS] ?: NO_VALUE_LONG
    }

    suspend fun setProgramsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_PROGRAMS] = timeInMillis
        }
    }

    private fun loadProgramsUpdateLastTime() = dataStore.data.map { preferences ->
        preferences[LAST_UPDATE_PROGRAMS] ?: NO_VALUE_LONG
    }


    suspend fun setAppSettings(appSettings: AppSettingsModel) {
        dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = appSettings.appTheme
            settings[PROGRAM_UPDATE_PERIOD_OPTION] = appSettings.programsUpdatePeriod
            settings[CHANNELS_UPDATE_PERIOD_OPTION] = appSettings.channelsUpdatePeriod
            settings[PROGRAM_VIEW_COUNT_OPTION] = appSettings.programsViewCount
        }
    }

    fun loadAppSettings() = dataStore.data.map { preferences ->
        val themeId = preferences[APP_THEME_OPTION] ?: AppThemeOptions.SYSTEM.id
        val programUpdatePeriod =
            preferences[PROGRAM_UPDATE_PERIOD_OPTION] ?: DEFAULT_PROGRAMS_UPDATE_PERIOD
        val channelsUpdatePeriod =
            preferences[CHANNELS_UPDATE_PERIOD_OPTION] ?: DEFAULT_CHANNELS_UPDATE_PERIOD
        val programsViewCount =
            preferences[PROGRAM_VIEW_COUNT_OPTION] ?: DEFAULT_PROGRAMS_VISIBLE_COUNT

        AppSettingsModel(
            programsUpdatePeriod = programUpdatePeriod,
            channelsUpdatePeriod = channelsUpdatePeriod,
            programsViewCount = programsViewCount,
            appTheme = themeId
        )

    }

    private companion object {
        val APP_THEME_OPTION = intPreferencesKey("theme_option")
        val PROGRAM_UPDATE_PERIOD_OPTION = intPreferencesKey("program_update_period_option")
        val CHANNELS_UPDATE_PERIOD_OPTION = intPreferencesKey("channels_update_period_option")
        val PROGRAM_VIEW_COUNT_OPTION = intPreferencesKey("program_view_count_option")

        val DEFAULT_CHANNEL_LIST = stringPreferencesKey("DefaultChannelList")
        val CURRENT_CHANNEL_LIST = stringPreferencesKey("CurrentChannelList")
        val LAST_UPDATE_CHANNELS = longPreferencesKey("LastUpdateChannels")
        val LAST_UPDATE_PROGRAMS = longPreferencesKey("LastUpdatePrograms")
    }
}