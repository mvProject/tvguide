package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import kotlinx.coroutines.flow.Flow

interface IChannelListRepository {
    fun loadChannelsListsAsFlow(): Flow<List<ChannelList>>
    suspend fun loadChannelsLists(): List<ChannelList>
    suspend fun addChannelsList(name: String)
    suspend fun addChannelList(list: ChannelList)
    suspend fun addChannelLists(lists: List<ChannelList>)
    suspend fun deleteList(item: ChannelList)
}
