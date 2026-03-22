package com.mvproject.tvprogramguide.data.model.backup

import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_LONG
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT
import kotlinx.serialization.Serializable

@Serializable
data class TvBackup(
    val timeStamp: Long = COUNT_ZERO_LONG,
    val theme: Int = AppThemeOptions.SYSTEM.id,
    val programsViewCount: Int = DEFAULT_PROGRAMS_VISIBLE_COUNT,
    val channelsUpdatePeriod: Int = DEFAULT_CHANNELS_UPDATE_PERIOD,
    val programsUpdatePeriod: Int = DEFAULT_PROGRAMS_UPDATE_PERIOD,
    val channelsData: List<PlaylistBackup> = emptyList()
) {
    @Serializable
    data class PlaylistBackup(
        val id: Int,
        val name: String,
        val isSelected: Boolean,
        val content: List<String>
    )
}