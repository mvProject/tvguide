package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionFromAvailable
import com.mvproject.tvprogramguide.data.mappers.Mappers.toAvailableChannelEntities
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import javax.inject.Inject

class AllChannelRepository
    @Inject
    constructor(
        private val allChannelDao: AllChannelDao,
    ) {
        suspend fun loadChannels(): List<SelectionChannel> =
            allChannelDao
                .getChannels()
                .map { item -> item.asSelectionFromAvailable() }

        @Transaction
        suspend fun updateChannels(channels: List<AvailableChannelResponse>) {
            allChannelDao.apply {
                deleteChannels()
                insertChannels(availableChannels = channels.toAvailableChannelEntities())
            }
        }
    }
