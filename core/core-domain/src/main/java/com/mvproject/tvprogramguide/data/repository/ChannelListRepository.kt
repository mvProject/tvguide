package com.mvproject.tvprogramguide.data.repository

import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asChannelLists
import com.mvproject.tvprogramguide.data.mappers.Mappers.toChannelsListEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import kotlinx.coroutines.flow.map
import kotlin.random.Random

/**
 * Repository class for managing channel list operations.
 *
 * @property channelsListDao Data Access Object for channel list operations.
 */
class ChannelListRepository(
    private val channelsListDao: ChannelsListDao,
) : IChannelListRepository {
    /**
     * Loads channel lists as a Flow, mapping database entities to domain models.
     *
     * @return A Flow of List<ChannelList> representing all channel lists.
     */
    override fun loadChannelsListsAsFlow() =
        channelsListDao
            .getChannelsListsAsFlow()
            .map { entities ->
                entities.asChannelLists()
            }

    /**
     * Loads channel lists as a suspend function, mapping database entities to domain models.
     *
     * @return A List<ChannelList> representing all channel lists.
     */
    override suspend fun loadChannelsLists() =
        channelsListDao
            .getChannelsLists()
            .asChannelLists()

    /**
     * Adds a new channel list with the given name.
     *
     * @param name The name of the new channel list.
     */
    override suspend fun addChannelsList(name: String) {
        if (name.isNotEmpty()) {
            val listItem =
                ChannelsListEntity(
                    id = Random.nextInt(),
                    name = name,
                    isSelected = false
                )
            channelsListDao.addChannelsList(channelList = listItem)
        }
    }

    /**
     * Adds a single channel list to the database.
     *
     * @param list The ChannelList to be added.
     */
    override suspend fun addChannelList(list: ChannelList) {
        channelsListDao.addChannelsList(channelList = list.toChannelsListEntity())
    }

    /**
     * Adds multiple channel lists to the database.
     *
     * @param lists The List<ChannelList> to be added.
     */
    override suspend fun addChannelLists(lists: List<ChannelList>) {
        val channelsLists = lists.map { it.toChannelsListEntity() }
        channelsListDao.addChannelsLists(channelLists = channelsLists)
    }

    /**
     * Deletes a single channel list from the database.
     *
     * @param item The ChannelList to be deleted.
     */
    override suspend fun deleteList(item: ChannelList) {
        channelsListDao.deleteSingleChannelsList(channelId = item.id)
    }
}
