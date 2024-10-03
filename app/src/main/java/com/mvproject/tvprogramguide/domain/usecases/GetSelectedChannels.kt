package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import javax.inject.Inject

/**
 * Use case to retrieving channels for list
 * @property selectedChannelRepository the SelectedChannelRepository repository
 */
class GetSelectedChannels
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository,
    ) {
        suspend operator fun invoke(listName: String): List<SelectionChannel> {
            val favoriteChannels =
                selectedChannelRepository
                    .loadSelectedChannels(listName = listName)
                    .sortedBy { item -> item.order }
            return favoriteChannels
        }
    }
