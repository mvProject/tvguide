package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionChannelToEntity
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case to retrieving channels for list
 * @property selectedChannelRepository the SelectedChannelRepository repository
 */
class SaveChannelsSelection
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository
    ) {
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
