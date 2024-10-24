package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionFromAvailable
import com.mvproject.tvprogramguide.data.mappers.Mappers.toAvailableChannelEntities
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import javax.inject.Inject

/**
 * Repository class for managing all channel-related operations.
 *
 * @property allChannelDao Data Access Object for channel operations.
 */
class AllChannelRepository
@Inject
constructor(
    private val allChannelDao: AllChannelDao,
) {
    /**
     * Loads all channels from the database and maps them to SelectionChannel objects.
     *
     * @return A list of SelectionChannel objects representing all available channels.
     */
    suspend fun loadChannels(): List<SelectionChannel> =
        allChannelDao
            .getChannels()
            .map { item -> item.asSelectionFromAvailable() }

    /**
     * Updates the channels in the database with a new list of AvailableChannelResponse objects.
     * This operation is performed as a transaction to ensure data consistency.
     *
     * @param channels A list of AvailableChannelResponse objects to update the database with.
     */
    @Transaction
    suspend fun updateChannels(channels: List<AvailableChannelResponse>) {
        allChannelDao.apply {
            deleteChannels()
            insertChannels(availableChannels = channels.toAvailableChannelEntities())
        }
    }
}
