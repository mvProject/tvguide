package com.mvproject.tvprogramguide

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceManager
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("settings")

class StoreManager @Inject constructor(context: Context) {

    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun setChannelsUpdateLastTime(time: Long?) {
        time?.let { value ->
            preferences.edit().apply {
                putLong(LAST_UPDATE_CHANNELS, value)
                apply()
            }
        }
    }

    val channelsUpdateLastTime
        get() = preferences.getLong(LAST_UPDATE_CHANNELS, -1L)

    fun setProgramsUpdateLastTime(time: Long?) {
        time?.let { value ->
            preferences.edit().apply {
                putLong(LAST_UPDATE_PROGRAMS, value)
                apply()
            }
        }
    }

    val programsUpdateLastTime
        get() = preferences.getLong(LAST_UPDATE_PROGRAMS, -1L)


    fun setProgramByChannelDefaultCount(count: Int?) {
        count?.let { value ->
            preferences.edit().apply {
                putInt(DEFAULT_PROGRAMS_COUNT, value)
                apply()
            }
        }
    }

    val programByChannelDefaultCount
        get() = preferences.getLong(DEFAULT_PROGRAMS_COUNT, -1L)

    fun setDefaultChannelList(name: String?) {
        name?.let { value ->
            preferences.edit().apply {
                putString(DEFAULT_CHANNEL_LIST, value)
                apply()
            }
        }
    }

    val defaultChannelList
        get() = preferences.getString(DEFAULT_CHANNEL_LIST, NO_VALUE_STRING) ?: NO_VALUE_STRING

    fun setCurrentChannelList(name: String?) {
        name?.let { value ->
            preferences.edit().apply {
                putString(CURRENT_CHANNEL_LIST, value)
                apply()
            }
        }
    }

    val currentChannelList
        get() = preferences.getString(CURRENT_CHANNEL_LIST, NO_VALUE_STRING) ?: NO_VALUE_STRING

  //  private val settingsDataStore = context.dataStore

  //  private val SELECTED_LIST = stringPreferencesKey("selectedList")

  //  val selectedList: Flow<String> = settingsDataStore.data
  //      .map { preferences ->
  //          // No type safety.
  //          preferences[SELECTED_LIST] ?: ""
  //      }

   // suspend fun saveSelectedList(name: String) {
   //     settingsDataStore.edit { settings ->
   //         settings[SELECTED_LIST] = name
   //     }
   // }

    companion object {
        private const val LAST_UPDATE_CHANNELS = "LastUpdateChannels"
        private const val LAST_UPDATE_PROGRAMS = "LastUpdatePrograms"
        private const val DEFAULT_PROGRAMS_COUNT = "DefaultProgramsCount"
        private const val DEFAULT_CHANNEL_LIST = "DefaultChannelList"
        private const val CURRENT_CHANNEL_LIST = "CurrentChannelList"
    }
}