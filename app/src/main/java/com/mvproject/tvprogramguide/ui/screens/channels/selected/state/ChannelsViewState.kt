package com.mvproject.tvprogramguide.ui.screens.channels.selected.state

import androidx.compose.runtime.Stable
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.obtainIndexOrZero
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ChannelsViewState(
    val listName: String = String.empty,
    val isLoading: Boolean = false,
    val isOnboard: Boolean = false,
    val playlists: ImmutableList<ChannelList> = persistentListOf(),
    val channels: ImmutableList<SelectedChannelWithPrograms> = persistentListOf(),
) {
    val selectedListIndex
        get() = playlists
            .map { channels -> channels.listName }
            .obtainIndexOrZero(target = listName)

    val isNotSinglePlaylist get() = playlists.count() > COUNT_ONE
}
