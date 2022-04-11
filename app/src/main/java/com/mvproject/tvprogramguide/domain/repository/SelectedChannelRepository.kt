package com.mvproject.tvprogramguide.domain.repository

import com.mvproject.tvprogramguide.data.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.domain.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.domain.utils.Mappers.asChannelsFromEntities
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class SelectedChannelRepository @Inject constructor(
    private val selectedChannelDao: SelectedChannelDao
) {
    suspend fun loadSelectedChannels(listName: String): List<Channel> {
        return selectedChannelDao.getSelectedChannels(listName).asChannelsFromEntities()
    }

    suspend fun loadSelectedChannelsIds() =
        selectedChannelDao.getSelectedChannelsIds()

    fun loadSelectedChannelsFlow(listName: String) =
        selectedChannelDao.getSelectedChannelsFlow(listName).map { it.asChannelsFromEntities() }

    suspend fun addChannel(selectedChannel: SelectedChannelEntity) {
        selectedChannelDao.insertChannel(selectedChannel)
    }

    suspend fun deleteChannel(id: String) {
        selectedChannelDao.deleteSelectedChannel(id)
    }
}
