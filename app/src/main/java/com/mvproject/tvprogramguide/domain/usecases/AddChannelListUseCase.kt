package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject
import kotlin.random.Random

/**
 * Use case for adding a new channel list.
 *
 * @property channelListRepository The repository for managing channel lists.
 */
class AddChannelListUseCase
@Inject
constructor(
    private val channelListRepository: ChannelListRepository,
) {
    /**
     * Adds a new channel list with the given name.
     *
     * This function performs the following steps:
     * 1. Loads existing channel lists.
     * 2. Creates a new ChannelList object with a random ID.
     * 3. Sets the new list as selected if it's the first list being added.
     * 4. Adds the new channel list to the repository.
     *
     * @param listName The name of the new channel list to be added.
     */
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
