package com.mvproject.tvprogramguide.domain.repository

import com.mvproject.tvprogramguide.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.utils.Mappers.asChannelsFromEntities
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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