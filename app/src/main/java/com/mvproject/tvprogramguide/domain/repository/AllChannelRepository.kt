package com.mvproject.tvprogramguide.domain.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.domain.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.entity.ChannelEntity
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.domain.EpgService
import com.mvproject.tvprogramguide.domain.utils.Mappers.asChannelEntities
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.domain.utils.Mappers.asChannelsFromEntities
import com.mvproject.tvprogramguide.domain.utils.Mappers.filterNoEpg
import javax.inject.Inject

class AllChannelRepository @Inject constructor(
    private val epgService: EpgService,
    private val allChannelDao: AllChannelDao
) {

    private var databaseChannels: List<ChannelEntity> = emptyList()

    suspend fun loadChannels(): List<Channel> {
        databaseChannels = allChannelDao.getChannelList()
        return if (databaseChannels.isEmpty()) {
            val networkChannels = epgService.getChannels().channels.filterNoEpg()
            databaseChannels = networkChannels.asChannelEntities()
            allChannelDao.insertChannelList(databaseChannels)
            databaseChannels.asChannelsFromEntities()
        } else {
            databaseChannels.asChannelsFromEntities()
        }
    }

    suspend fun loadPrograms(channels: List<String>): List<Channel> {
        return allChannelDao.getChannelList().asChannelsFromEntities()
    }

    @Transaction
    suspend fun loadProgramFromSource() {
        val ch = epgService.getChannels().channels.filterNoEpg()
        val entities = ch.asChannelEntities()
        if (entities.count() > COUNT_ZERO) {
            allChannelDao.apply {
                deleteChannels()
                insertChannelList(entities)
            }
        }

    }
}