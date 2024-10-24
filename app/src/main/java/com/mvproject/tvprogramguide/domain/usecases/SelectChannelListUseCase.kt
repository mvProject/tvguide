package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject

/**
 * Use case for selecting a specific channel list.
 *
 * @property channelListRepository The repository for managing channel lists.
 */
class SelectChannelListUseCase
@Inject
constructor(
    private val channelListRepository: ChannelListRepository
) {
    /**
     * Selects the specified channel list and updates the selection state of all lists.
     *
     * This function performs the following steps:
     * 1. Loads all existing channel lists.
     * 2. Creates a new list of ChannelList objects with updated selection states.
     * 3. Saves the updated list of channel lists to the repository.
     *
     * @param list The ChannelList to be selected.
     */
    suspend operator fun invoke(list: ChannelList) {
        val playlists = channelListRepository.loadChannelsLists()
        val changedPlaylists = playlists.map {
            it.copy(isSelected = it.id == list.id)
        }
        channelListRepository.addChannelLists(lists = changedPlaylists)
    }
}
