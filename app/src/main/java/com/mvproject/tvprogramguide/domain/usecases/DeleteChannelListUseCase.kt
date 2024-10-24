package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject

/**
 * Use case for deleting a specified channel list.
 *
 * @property channelListRepository The repository for managing channel lists.
 */
class DeleteChannelListUseCase
@Inject
constructor(
    private val channelListRepository: ChannelListRepository
) {
    /**
     * Deletes the specified channel list and handles the selection state.
     *
     * This function performs the following steps:
     * 1. Checks if the list to be deleted is currently selected.
     * 2. If it is selected, finds another list to be set as selected.
     * 3. Updates the selection state of the new list if found.
     * 4. Deletes the specified list.
     *
     * @param list The ChannelList to be deleted.
     */
    suspend operator fun invoke(list: ChannelList) {
        if (list.isSelected) {
            // Find another list to be set as selected
            val updateSelected = channelListRepository.loadChannelsLists()
                .firstOrNull { !it.isSelected }
            // Update the selection state of the new list if found
            updateSelected?.let { selected ->
                channelListRepository.addChannelList(selected.copy(isSelected = true))
            }
        }
        channelListRepository.deleteList(item = list)
    }
}
