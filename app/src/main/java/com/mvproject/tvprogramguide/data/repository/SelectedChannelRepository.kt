package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectionFromSelected
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectedChannelRepository
@Inject
constructor(
    private val selectedChannelDao: SelectedChannelDao,
) {
    suspend fun loadSelectedChannels(listName: String): List<SelectionChannel> =
        selectedChannelDao
            .getSelectedChannels(listName = listName)
            .map { item -> item.asSelectionFromSelected() }

    fun loadSelectedChannelsAsFlow(): Flow<List<SelectionChannel>> =
        selectedChannelDao
            .getChannelsForCurrentListAsFlow()
            .map { list ->
                list.map { item -> item.asSelectionFromSelected() }
            }

    @Transaction
    suspend fun addChannels(
        listName: String,
        selectedChannels: List<SelectedChannelEntity>,
    ) {
        selectedChannelDao.deleteChannels(list = listName)
        selectedChannelDao.insertChannels(channel = selectedChannels)
    }
}
