package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.Mappers.asChannelsFromEntities
import com.mvproject.tvprogramguide.data.utils.Mappers.toAvailableChannelEntities
import com.mvproject.tvprogramguide.data.utils.filterNoEpg
import javax.inject.Inject

class AllChannelRepository @Inject constructor(
    private val epgService: EpgService,
    private val allChannelDao: AllChannelDao
) {
    private var databaseChannels: List<AvailableChannelEntity> = emptyList()

    suspend fun loadChannels(): List<AvailableChannel> {
        databaseChannels = allChannelDao.getChannelList()
        return if (databaseChannels.isEmpty()) {
            val networkChannels = epgService.getChannels()
                .channels
                .filterNoEpg()
            databaseChannels = networkChannels.toAvailableChannelEntities()
            allChannelDao.insertChannelList(availableChannels = databaseChannels)
            databaseChannels.asChannelsFromEntities()
        } else {
            databaseChannels.asChannelsFromEntities()
        }
    }

    @Transaction
    suspend fun loadProgramFromSource() {
        val networkChannels = epgService.getChannels()
            .channels
            .filterNoEpg()

        val entities = networkChannels.toAvailableChannelEntities()
        if (entities.count() > COUNT_ZERO) {
            updateEntities(entities = entities)
        }
    }

    suspend fun updateEntities(entities: List<AvailableChannelEntity>) {
        allChannelDao.apply {
            deleteChannels()
            insertChannelList(availableChannels = entities)
        }
    }
}
