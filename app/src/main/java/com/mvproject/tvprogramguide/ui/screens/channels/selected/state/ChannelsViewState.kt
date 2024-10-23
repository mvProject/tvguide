package com.mvproject.tvprogramguide.ui.screens.channels.selected.state

import androidx.compose.runtime.Stable
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.obtainIndexOrZero

@Stable
data class ChannelsViewState(
    val listName: String = String.empty,
    val isLoading: Boolean = true,
    val isOnboard: Boolean = false,
    val playlists: List<ChannelList> = emptyList(),
    val channels: List<SelectedChannelWithPrograms> = emptyList(),
) {
    val selectedListIndex
        get() = playlists
            .map { channels -> channels.listName }
            .obtainIndexOrZero(target = listName)

    val isNotSinglePlaylist get() = playlists.count() > COUNT_ONE
}
