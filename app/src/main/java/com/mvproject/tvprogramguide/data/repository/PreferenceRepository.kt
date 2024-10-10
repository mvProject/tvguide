package com.mvproject.tvprogramguide.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_LONG
import com.mvproject.tvprogramguide.utils.AppConstants.json
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class PreferenceRepository
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun setOnBoardState(onBoardState: Boolean) {
        dataStore.edit { settings ->
            settings[ON_BOARD_COMPLETE] = onBoardState
        }
    }

    fun loadOnBoardState() =
        dataStore.data
            .map { preferences ->
                preferences[ON_BOARD_COMPLETE] ?: true
            }


    val isNeedFullProgramsUpdate =
        combine(
            loadAppSettings(),
            loadProgramsUpdateLastTime(),
        ) { settings, lastUpdate ->
            val current = Clock.System.now()
            val last = Instant.fromEpochMilliseconds(lastUpdate)
            val updateRequest = current - last > settings.programsUpdatePeriod.days

            return@combine updateRequest
        }

    val isNeedAvailableChannelsUpdate =
        combine(
            loadAppSettings(),
            loadChannelsUpdateLastTime(),
        ) { settings, lastUpdate ->
            val current = Clock.System.now()
            val last = Instant.fromEpochMilliseconds(lastUpdate)

            return@combine current - last > settings.channelsUpdatePeriod.days
        }

    suspend fun setChannelsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_CHANNELS] = timeInMillis
        }
    }

    private fun loadChannelsUpdateLastTime() =
        dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_CHANNELS] ?: NO_VALUE_LONG
        }

    suspend fun setProgramsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_PROGRAMS] = timeInMillis
        }
    }

    private fun loadProgramsUpdateLastTime() =
        dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_PROGRAMS] ?: NO_VALUE_LONG
        }

    suspend fun setChannelsForUpdate(ids: List<String>) {
        dataStore.edit { settings ->
            settings[CHANNELS_FOR_UPDATE] = json.encodeToString(ids)
        }
    }

    fun getChannelsForUpdate() =
        dataStore.data.map { preferences ->
            preferences[CHANNELS_FOR_UPDATE]?.let {
                json.decodeFromString<List<String>>(it)
            } ?: emptyList()
        }

    suspend fun setAppSettings(appSettings: AppSettingsModel) {
        dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = appSettings.appTheme
            settings[PROGRAM_UPDATE_PERIOD_OPTION] = appSettings.programsUpdatePeriod
            settings[CHANNELS_UPDATE_PERIOD_OPTION] = appSettings.channelsUpdatePeriod
            settings[PROGRAM_VIEW_COUNT_OPTION] = appSettings.programsViewCount
        }
    }

    fun loadAppSettings() =
        dataStore.data.map { preferences ->
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
                appTheme = themeId,
            )
        }

    suspend fun setProgramsCleanTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[PROGRAM_CLEAN] = timeInMillis
        }
    }

    suspend fun getProgramsCleanTime() =
        dataStore.data.map { preferences ->
            preferences[PROGRAM_CLEAN] ?: NO_VALUE_LONG
        }.first()

    private companion object {
        val APP_THEME_OPTION = intPreferencesKey("theme_option")
        val PROGRAM_UPDATE_PERIOD_OPTION = intPreferencesKey("program_update_period_option")
        val CHANNELS_UPDATE_PERIOD_OPTION = intPreferencesKey("channels_update_period_option")
        val PROGRAM_VIEW_COUNT_OPTION = intPreferencesKey("program_view_count_option")

        val PROGRAM_CLEAN = longPreferencesKey("programClean")
        val LAST_UPDATE_CHANNELS = longPreferencesKey("LastUpdateChannels")
        val LAST_UPDATE_PROGRAMS = longPreferencesKey("LastUpdatePrograms")

        val ON_BOARD_COMPLETE = booleanPreferencesKey("onBoardComplete")
        val CHANNELS_FOR_UPDATE = stringPreferencesKey("ChannelsForUpdate")
    }
}
