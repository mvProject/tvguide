package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject
import kotlin.random.Random

/**
 * Use case to adding new playlist
 * @property channelListRepository the CustomListRepository repository
 */
class AddChannelListUseCase
@Inject
constructor(
    private val channelListRepository: ChannelListRepository,
) {
    suspend operator fun invoke(listName: String) {
        val playlists = channelListRepository.loadChannelsLists()
        val channelList = ChannelList(
            id = Random.nextInt(),
            listName = listName,
            isSelected = playlists.isEmpty()
        )
        channelListRepository.addChannelList(list = channelList)
    }
}
