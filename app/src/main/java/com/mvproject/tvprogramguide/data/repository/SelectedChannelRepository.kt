package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import javax.inject.Inject

class SelectedChannelRepository
    @Inject
    constructor(
        private val selectedChannelDao: SelectedChannelDao,
    ) {
        @Transaction
        suspend fun loadSelectedChannels(listName: String): List<SelectedChannelWithIconEntity> {
            return selectedChannelDao.getSelectedChannels(listName = listName)
        }

        suspend fun loadSelectedChannelsIds() = selectedChannelDao.getSelectedChannelsIds()

        suspend fun loadChannelNameById(selectedId: String) = selectedChannelDao.getChannelNameById(id = selectedId)

        @Transaction
        fun loadSelectedChannelsFlow(listName: String) = selectedChannelDao.getSelectedChannelsFlow(listName = listName)

        suspend fun addChannel(selectedChannel: SelectedChannelEntity) {
            selectedChannelDao.insertChannel(channel = selectedChannel)
        }

        @Transaction
        suspend fun addChannels(
            listName: String,
            selectedChannels: List<SelectedChannelEntity>,
        ) {
            selectedChannelDao.deleteChannels(list = listName)
            selectedChannelDao.insertChannels(channel = selectedChannels)
        }

        @Transaction
        suspend fun updateChannels(channels: List<SelectedChannelEntity>) {
            selectedChannelDao.updateChannels(channelsForUpdate = channels)
        }

        suspend fun deleteChannel(channelId: String) {
            selectedChannelDao.deleteSelectedChannel(channelId = channelId)
        }
    }
