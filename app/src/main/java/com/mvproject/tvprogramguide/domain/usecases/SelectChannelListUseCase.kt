package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject

/**
 * Use case to adding new playlist
 * @property channelListRepository the CustomListRepository repository
 */
class SelectChannelListUseCase
    @Inject
    constructor(
        private val channelListRepository: ChannelListRepository
    ) {
        suspend operator fun invoke(list: ChannelList) {
            val playlists = channelListRepository.loadChannelsLists()
            val changedPlaylists = playlists.map {
                it.copy(isSelected = it.id == list.id)
            }
            channelListRepository.addChannelLists(lists = changedPlaylists)
        }
    }
