package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case for retrieving available channels, including their selection status for a specific list.
 *
 * @property allChannelRepository The repository for accessing all available channels.
 * @property selectedChannelRepository The repository for accessing selected channels.
 */
class GetAvailableChannels
@Inject
constructor(
    private val allChannelRepository: AllChannelRepository,
    private val selectedChannelRepository: SelectedChannelRepository,
) {
    /**
     * Retrieves and processes the list of available channels, marking their selection status.
     *
     * This function performs the following steps:
     * 1. Loads all available channels from the allChannelRepository.
     * 2. Loads selected channels for the specified list from the selectedChannelRepository.
     * 3. Creates a set of selected channel IDs for efficient lookup.
     * 4. Maps the available channels, marking each as selected if its ID is in the selected set.
     * 5. Returns the list of mapped channels with their selection status and parent list name.
     *
     * @param listName The name of the channel list to check for selected channels.
     * @return A list of SelectionChannel objects with updated selection status and parent list name.
     */
    suspend operator fun invoke(listName: String): List<SelectionChannel> {
        val availableChannels = allChannelRepository.loadChannels()

        val favoriteChannels = selectedChannelRepository.loadSelectedChannels(listName = listName)

        val selectedIds = favoriteChannels.map { it.channelId }
        // Map available channels, updating their selection status and parent list
        val mappedChannels =
            availableChannels
                .map { chn ->
                    chn.copy(
                        isSelected = chn.channelId in selectedIds,
                        parentList = listName,
                    )
                }

        return mappedChannels
    }
}
