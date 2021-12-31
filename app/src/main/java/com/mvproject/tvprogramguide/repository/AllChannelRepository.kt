package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.database.entity.ChannelEntity
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.utils.Mappers.asChannelEntities
import com.mvproject.tvprogramguide.utils.Mappers.asChannelsFromEntities
import com.mvproject.tvprogramguide.utils.Mappers.filterNoEpg
import timber.log.Timber
import javax.inject.Inject

class AllChannelRepository @Inject constructor(
    private val epgService: EpgService,
    private val allChannelDao: AllChannelDao
) {
    init {
        Timber.d("Injection AllChannelRepository")
    }

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
}