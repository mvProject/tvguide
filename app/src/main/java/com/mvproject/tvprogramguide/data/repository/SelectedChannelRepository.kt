package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionFromSelected
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository class for managing selected channel operations.
 *
 * @property selectedChannelDao Data Access Object for selected channel operations.
 */
class SelectedChannelRepository
@Inject
constructor(
    private val selectedChannelDao: SelectedChannelDao,
) {
    /**
     * Loads selected channels for a specific list.
     *
     * @param listName The name of the channel list to fetch selected channels for.
     * @return A List<SelectionChannel> representing the selected channels for the specified list.
     */
    suspend fun loadSelectedChannels(listName: String): List<SelectionChannel> =
        selectedChannelDao
            .getSelectedChannels(listName = listName)
            .map { item -> item.asSelectionFromSelected() }

    /**
     * Loads selected channels for the current list as a Flow.
     *
     * @return A Flow of List<SelectionChannel> representing the selected channels for the current list.
     */
    fun loadSelectedChannelsAsFlow(): Flow<List<SelectionChannel>> =
        selectedChannelDao
            .getChannelsForCurrentListAsFlow()
            .map { list ->
                list.map { item -> item.asSelectionFromSelected() }
            }

    /**
     * Adds or updates channels for a specific list.
     * This operation is performed as a transaction to ensure data consistency.
     *
     * @param listName The name of the channel list to add or update channels for.
     * @param selectedChannels The List<SelectedChannelEntity> containing the channels to be added or updated.
     */
    @Transaction
    suspend fun addChannels(
        listName: String,
        selectedChannels: List<SelectedChannelEntity>,
    ) {
        selectedChannelDao.deleteChannels(list = listName)
        selectedChannelDao.insertChannels(channel = selectedChannels)
    }
}
