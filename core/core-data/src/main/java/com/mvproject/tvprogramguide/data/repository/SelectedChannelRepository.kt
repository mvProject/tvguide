package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionChannelToEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionFromSelected
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository class for managing selected channel operations in the TV Program Guide.
 * This repository handles operations for storing, retrieving, and managing user-selected channels
 * within different channel lists.
 *
 * @property selectedChannelDao Data Access Object for selected channel database operations.
 */
class SelectedChannelRepository(
    private val selectedChannelDao: SelectedChannelDao,
) : ISelectedChannelRepository {
    /**
     * Loads selected channels for a specific channel list.
     *
     * @param listName The name of the channel list to fetch selected channels for
     * @return A list of [SelectionChannel] objects representing the selected channels in the specified list
     */
    override suspend fun loadSelectedChannels(listName: String): List<SelectionChannel> =
        selectedChannelDao
            .getSelectedChannels(listName = listName)
            .map { item -> item.asSelectionFromSelected() }

    /**
     * Provides a continuous flow of selected channels for the current list.
     * This flow will emit new values whenever the selected channels in the current list change.
     *
     * @return A [Flow] of List<[SelectionChannel]> that updates whenever the channel selection changes
     */
    override fun loadSelectedChannelsAsFlow(): Flow<List<SelectionChannel>> =
        selectedChannelDao
            .getChannelsForCurrentListAsFlow()
            .map { list ->
                list.map { item -> item.asSelectionFromSelected() }
            }

    /**
     * Adds or updates channels for a specific channel list.
     * This operation replaces all existing channels in the specified list with the new selection.
     * The operation is performed as a transaction to ensure data consistency.
     *
     * @param listName The name of the channel list to update
     * @param selectedChannels The list of channels to be saved in the specified list
     */
    @Transaction
    override suspend fun addChannels(
        listName: String,
        selectedChannels: List<SelectionChannel>,
    ) {
        val channelsUpdate =
            selectedChannels.asSelectionChannelToEntity()

        selectedChannelDao.deleteChannels(list = listName)
        selectedChannelDao.insertChannels(channel = channelsUpdate)
    }
}
