package com.mvproject.tvprogramguide.data.model.settings

import androidx.annotation.StringRes
import com.mvproject.tvprogramguide.R

/**
 * Enum to represent the app theme selected by the user.
 *
 * @property id the theme id
 * @property titleRes the string title resource
 */
enum class AppThemeOptions(val id: Int, @StringRes val titleRes: Int) {

    /**
     * Light app theme.
     */
    LIGHT(0, R.string.settings_theme_light),

    /**
     * Dark app theme.
     */
    DARK(1, R.string.settings_theme_dark),

    /**
     * System-based app theme.
     */
    SYSTEM(2, R.string.settings_theme_system);

    companion object {
        fun getThemeById(targetId: Int) = values().find { theme -> theme.id == targetId } ?: SYSTEM
    }

}