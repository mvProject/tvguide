package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionChannelToEntity
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case for saving the selected channels for a specific list.
 *
 * @property selectedChannelRepository The repository for managing selected channel data.
 */
class SaveChannelsSelection
@Inject
constructor(
    private val selectedChannelRepository: SelectedChannelRepository
) {
    /**
     * Saves the selected channels for a specified list.
     *
     * This function performs the following steps:
     * 1. Converts the list of SelectionChannel objects to SelectedChannelEntity objects.
     * 2. Calls the repository to add (update) the channels for the specified list.
     *
     * @param listName The name of the list to save the selected channels for.
     * @param channels The list of SelectionChannel objects to be saved.
     */
    suspend operator fun invoke(
        listName: String,
        channels: List<SelectionChannel>,
        //        channelsForUpdate:List<String> = emptyList()
    ) {
        val channelsUpdate =
            channels.asSelectionChannelToEntity()

        selectedChannelRepository.addChannels(
            listName = listName,
            selectedChannels = channelsUpdate,
        )
    }
}
