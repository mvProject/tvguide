package com.mvproject.tvprogramguide.ui.screens.selectedchannels.state

import androidx.compose.runtime.Stable
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.obtainIndexOrZero

@Stable
data class ChannelsViewState(
    val listName: String = NO_VALUE_STRING,
    val isOnlineUpdating: Boolean = false,
    val programs: List<SelectedChannelWithPrograms> = listOf(),
    val allPlaylists: AllPlaylists = AllPlaylists(),
    val playlistContent: PlaylistContent = PlaylistContent(),
) {
    val listNames
        get() = allPlaylists.playlists.map { channels -> channels.listName }

    val selectedListIndex
        get() = listNames.obtainIndexOrZero(target = listName)
}

@Stable
data class AllPlaylists(
    val playlists: List<UserChannelsList> = emptyList()
)

@Stable
data class PlaylistContent(
    val channels: List<SelectedChannelWithPrograms> = emptyList()
)