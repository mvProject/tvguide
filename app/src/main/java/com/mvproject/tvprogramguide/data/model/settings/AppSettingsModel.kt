package com.mvproject.tvprogramguide.data.model.settings

import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT

data class AppSettingsModel(
    val channelsUpdatePeriod: Int = DEFAULT_CHANNELS_UPDATE_PERIOD,
    val programsUpdatePeriod: Int = DEFAULT_PROGRAMS_UPDATE_PERIOD,
    val programsViewCount: Int = DEFAULT_PROGRAMS_VISIBLE_COUNT,
    val appTheme: Int = AppThemeOptions.SYSTEM.id,
)
