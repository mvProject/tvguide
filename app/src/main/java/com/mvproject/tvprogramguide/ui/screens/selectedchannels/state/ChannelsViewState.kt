package com.mvproject.tvprogramguide.ui.screens.selectedchannels.state

import androidx.compose.runtime.Stable
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.obtainIndexOrZero

@Stable
data class ChannelsViewState(
    val listName: String = NO_VALUE_STRING,
    val isLoading: Boolean = true,
    val playlists: List<UserChannelsList> = emptyList(),
) {
    val listNames
        get() = playlists.map { channels -> channels.listName }

    val selectedListIndex
        get() = listNames.obtainIndexOrZero(target = listName)

    val isNotSinglePlaylist get() = playlists.count() > COUNT_ONE
}
