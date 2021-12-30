package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.utils.Mappers.asChannelsFromEntities
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class SelectedChannelRepository @Inject constructor(
    private val selectedChannelDao: SelectedChannelDao
) {
    init {
        Timber.d("testing Injection SelectedChannelRepository")
    }

    suspend fun loadSelectedChannels(listName: String): List<Channel> {
        val databaseChannels = selectedChannelDao.getSelectedChannels(listName)
        return databaseChannels.asChannelsFromEntities()
    }

    fun loadSelectedChannelsFlow(listName: String) = selectedChannelDao.getSelectedChannelsFlow(listName).map { it.asChannelsFromEntities() }

    suspend fun addChannel(selectedChannel: SelectedChannelEntity) {
        selectedChannelDao.insertChannel(selectedChannel)
    }

    suspend fun deleteChannel(id: String) {
        selectedChannelDao.deleteSelectedChannel(id)
    }
}