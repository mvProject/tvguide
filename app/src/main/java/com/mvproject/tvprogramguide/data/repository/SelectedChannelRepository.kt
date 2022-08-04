package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import javax.inject.Inject

class SelectedChannelRepository @Inject constructor(
    private val selectedChannelDao: SelectedChannelDao,
) {
    suspend fun loadSelectedChannels(listName: String): List<SelectedChannelEntity> {
        return selectedChannelDao.getSelectedChannels(listName = listName)
    }

    suspend fun loadSelectedChannelsIds() =
        selectedChannelDao.getSelectedChannelsIds()

    suspend fun loadChannelNameById(selectedId: String) =
        selectedChannelDao.getChannelNameById(id = selectedId)

    fun loadSelectedChannelsFlow(listName: String) =
        selectedChannelDao.getSelectedChannelsFlow(listName = listName)

    suspend fun addChannel(selectedChannel: SelectedChannelEntity) {
        selectedChannelDao.insertChannel(channel = selectedChannel)
    }

    @Transaction
    suspend fun updateChannels(channels: List<SelectedChannelEntity>) {
        selectedChannelDao.updateChannels(channelsForUpdate = channels)
    }

    suspend fun deleteChannel(channelId: String) {
        selectedChannelDao.deleteSelectedChannel(channelId = channelId)
    }
}
