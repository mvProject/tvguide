package com.mvproject.tvprogramguide.helpers

import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateUtils
import androidx.preference.PreferenceManager
import com.mvproject.tvprogramguide.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreHelper @Inject constructor(@ApplicationContext private val context: Context) {

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
        get() = preferences.getLong(LAST_UPDATE_CHANNELS, NO_VALUE_LONG)

    val channelsUpdatePeriod
        get() = preferences.getInt(UPDATE_CHANNEL_LIST_PERIOD, DEFAULT_CHANNELS_UPDATE_PERIOD)

    fun setProgramsUpdateLastTime(time: Long?) {
        time?.let { value ->
            preferences.edit().apply {
                putLong(LAST_UPDATE_PROGRAMS, value)
                apply()
            }
        }
    }

    val programsUpdateLastTime
        get() = preferences.getLong(LAST_UPDATE_PROGRAMS, NO_VALUE_LONG)

    fun setProgramsUpdatePeriod(period: Int?) {
        period?.let { value ->
            preferences.edit().apply {
                putInt(UPDATE_PROGRAM_LIST_PERIOD, value)
                apply()
            }
        }
    }

    val programsUpdatePeriod
        get() = preferences.getInt(UPDATE_PROGRAM_LIST_PERIOD, DEFAULT_PROGRAMS_UPDATE_PERIOD)

    val isNeedFullProgramsUpdate
        get() = defaultChannelList.isNotEmpty() &&
                System.currentTimeMillis() - programsUpdateLastTime >
                DateUtils.DAY_IN_MILLIS * programsUpdatePeriod

    val isNeedAvailableChannelsUpdate
        get() = System.currentTimeMillis() - channelsUpdateLastTime >
                DateUtils.DAY_IN_MILLIS * channelsUpdatePeriod

    fun setProgramByChannelDefaultCount(count: Int?) {
        count?.let { value ->
            preferences.edit().apply {
                putInt(DEFAULT_PROGRAMS_COUNT, value)
                apply()
            }
        }
    }

    val programByChannelDefaultCount
        get() = preferences.getInt(DEFAULT_PROGRAMS_COUNT, DEFAULT_PROGRAMS_VISIBLE_COUNT)

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

    fun setSelectedTheme(mode: Int?) {
        mode?.let { value ->
            preferences.edit().putInt(CURRENT_THEME_MODE, value).apply()
        }
    }

    fun setSelectedLanguage(lang: String?) {
        lang?.let { value ->
            preferences.edit().putString(CURRENT_LANG_MODE, value).apply()
        }
    }

    val selectedTheme
        get() = preferences.getInt(CURRENT_THEME_MODE, NO_VALUE_INT)

    val selectedLang
        get() = preferences.getString(CURRENT_LANG_MODE, NO_VALUE_STRING) ?: NO_VALUE_STRING

    companion object {
        private const val LAST_UPDATE_CHANNELS = "LastUpdateChannels"
        private const val LAST_UPDATE_PROGRAMS = "LastUpdatePrograms"
        private const val DEFAULT_PROGRAMS_COUNT = "DefaultProgramsCount"
        private const val DEFAULT_CHANNEL_LIST = "DefaultChannelList"
        private const val CURRENT_CHANNEL_LIST = "CurrentChannelList"

        private const val UPDATE_PROGRAM_LIST_PERIOD = "UpdateProgramListPeriod"
        private const val UPDATE_CHANNEL_LIST_PERIOD = "UpdateChannelListPeriod"

        private const val CURRENT_THEME_MODE = "CurrentThemeMode"
        private const val CURRENT_LANG_MODE = "CurrentLangMode"
    }
}