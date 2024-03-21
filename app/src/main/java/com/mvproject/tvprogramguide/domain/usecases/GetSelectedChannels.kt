package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionChannels
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import timber.log.Timber
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
                    .asSelectionChannels()
                    .sortedBy { chn ->
                        chn.order
                    }
            Timber.w("testing GetSelectedChannels count ${favoriteChannels.count()}")
            return favoriteChannels
        }
    }
