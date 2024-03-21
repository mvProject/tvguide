package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.asAvailableSelectionChannels
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionChannels
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case to adding new playlist
 * @property allChannelRepository the AllChannelRepository repository
 * @property selectedChannelRepository the SelectedChannelRepository repository
 */
class GetAvailableChannels
    @Inject
    constructor(
        private val allChannelRepository: AllChannelRepository,
        private val selectedChannelRepository: SelectedChannelRepository,
    ) {
        suspend operator fun invoke(listName: String): List<SelectionChannel> {
            val availableChannels =
                allChannelRepository
                    .loadChannels()
                    .asAvailableSelectionChannels()

            val favoriteChannels =
                selectedChannelRepository
                    .loadSelectedChannels(listName = listName)
                    .asSelectionChannels()

            val selectedIds = favoriteChannels.map { it.channelId }

            val mappedChannels =
                availableChannels
                    .map { chn ->
                        chn.copy(
                            isSelected = chn.channelId in selectedIds,
                            parentList = listName,
                        )
                    }

        /*            val mappedChannels2 =
                        buildList {
                            availableChannels.forEach { chn ->
                                val isInFavorites = chn.channelId in selectedIds
                                add(chn.copy(isSelected = isInFavorites))
                            }
                        }*/

            return mappedChannels
        }
    }
