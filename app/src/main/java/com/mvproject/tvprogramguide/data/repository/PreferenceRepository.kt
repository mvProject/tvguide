package com.mvproject.tvprogramguide.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_LONG
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

/**
 * Repository class for managing app preferences using DataStore.
 *
 * @property dataStore DataStore instance for storing preferences.
 */
class PreferenceRepository
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
) {
    /**
     * Sets the onboarding completion state.
     *
     * @param onBoardState Boolean indicating whether onboarding is complete.
     */
    suspend fun setOnBoardState(onBoardState: Boolean) {
        dataStore.edit { settings ->
            settings[ON_BOARD_COMPLETE] = onBoardState
        }
    }

    /**
     * Loads the onboarding completion state.
     *
     * @return Flow<Boolean> indicating whether onboarding is complete.
     */
    fun loadOnBoardState() =
        dataStore.data
            .map { preferences ->
                preferences[ON_BOARD_COMPLETE] ?: true
            }

    /**
     * Loads the onboarding completion state.
     *
     * @return Flow<Boolean> indicating whether onboarding is complete.
     */
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

    /**
     * Determines if an available channels update is needed based on the last update time and update period.
     */
    val isNeedAvailableChannelsUpdate =
        combine(
            loadAppSettings(),
            loadChannelsUpdateLastTime(),
        ) { settings, lastUpdate ->
            val current = Clock.System.now()
            val last = Instant.fromEpochMilliseconds(lastUpdate)

            return@combine current - last > settings.channelsUpdatePeriod.days
        }

    /**
     * Sets the last update time for channels.
     *
     * @param timeInMillis The update time in milliseconds.
     */
    suspend fun setChannelsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_CHANNELS] = timeInMillis
        }
    }

    /**
     * Loads the last update time for channels.
     */
    private fun loadChannelsUpdateLastTime() =
        dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_CHANNELS] ?: NO_VALUE_LONG
        }

    /**
     * Sets the last update time for programs.
     *
     * @param timeInMillis The update time in milliseconds.
     */
    suspend fun setProgramsUpdateLastTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[LAST_UPDATE_PROGRAMS] = timeInMillis
        }
    }

    /**
     * Loads the last update time for programs.
     */
    private fun loadProgramsUpdateLastTime() =
        dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_PROGRAMS] ?: NO_VALUE_LONG
        }

    /**
     * Sets the state indicating whether a programs update is required.
     *
     * @param state Boolean indicating if an update is required.
     */
    suspend fun setProgramsUpdateRequiredState(state: Boolean) {
        dataStore.edit { settings ->
            settings[PROGRAMS_UPDATE_REQUIRED] = state
        }
    }

    /**
     * Gets the state indicating whether a programs update is required.
     *
     * @return Flow<Boolean> indicating if an update is required.
     */
    fun getProgramsUpdateRequiredState() =
        dataStore.data.map { preferences ->
            preferences[PROGRAMS_UPDATE_REQUIRED] ?: false
        }

    /**
     * Sets the app settings.
     *
     * @param appSettings The AppSettingsModel to be saved.
     */
    suspend fun setAppSettings(appSettings: AppSettingsModel) {
        dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = appSettings.appTheme
            settings[PROGRAM_UPDATE_PERIOD_OPTION] = appSettings.programsUpdatePeriod
            settings[CHANNELS_UPDATE_PERIOD_OPTION] = appSettings.channelsUpdatePeriod
            settings[PROGRAM_VIEW_COUNT_OPTION] = appSettings.programsViewCount
        }
    }

    /**
     * Loads the app settings.
     *
     * @return Flow<AppSettingsModel> containing the current app settings.
     */
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

    /**
     * Sets the time for cleaning programs.
     *
     * @param timeInMillis The clean time in milliseconds.
     */
    suspend fun setProgramsCleanTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[PROGRAM_CLEAN] = timeInMillis
        }
    }

    /**
     * Gets the time for cleaning programs.
     *
     * @return The clean time in milliseconds.
     */
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

        // val CHANNELS_FOR_UPDATE = stringPreferencesKey("ChannelsForUpdate")
        val PROGRAMS_UPDATE_REQUIRED = booleanPreferencesKey("ProgramsUpdateRequired")
    }
}
