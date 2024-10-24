package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case for retrieving selected channels for a specific list.
 *
 * @property selectedChannelRepository The repository for accessing selected channel data.
 */
class GetSelectedChannels
@Inject
constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
) {
    /**
     * Retrieves and sorts the selected channels for a specified list.
     *
     * This function performs the following steps:
     * 1. Loads selected channels for the specified list from the selectedChannelRepository.
     * 2. Sorts the channels based on their order property.
     *
     * @param listName The name of the list to retrieve selected channels for.
     * @return A sorted list of SelectionChannel objects representing the selected channels.
     */
    suspend operator fun invoke(listName: String): List<SelectionChannel> {
        val favoriteChannels =
            selectedChannelRepository
                .loadSelectedChannels(listName = listName)
                .sortedBy { item -> item.order }
        return favoriteChannels
    }
}
