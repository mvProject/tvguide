package com.mvproject.tvprogramguide.data.repository

import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asChannelLists
import com.mvproject.tvprogramguide.data.mappers.Mappers.toChannelsListEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class ChannelListRepository
@Inject
constructor(
    private val channelsListDao: ChannelsListDao,
) {
    fun loadChannelsListsAsFlow() =
        channelsListDao
            .getAllUserChannelsListsAsFlow()
            .map { entities ->
                entities.asChannelLists()
            }

    suspend fun loadChannelsLists() =
        channelsListDao
            .getAllUserChannelsLists()
            .asChannelLists()


    suspend fun addCustomList(name: String) {
        if (name.isNotEmpty()) {
            val listItem =
                ChannelsListEntity(
                    id = Random.nextInt(),
                    name = name,
                    isSelected = false
                )
            channelsListDao.addUserChannelsList(channelList = listItem)
        }
    }

    suspend fun addChannelList(list: ChannelList) {
        channelsListDao.addUserChannelsList(channelList = list.toChannelsListEntity())
    }

    suspend fun addChannelLists(lists: List<ChannelList>) {
        val channelsLists = lists.map { it.toChannelsListEntity() }
        channelsListDao.addUserChannelsLists(channelLists = channelsLists)
    }

    suspend fun deleteList(item: ChannelList) {
        channelsListDao.deleteSingleUserChannelsList(channelId = item.id)
    }
}
