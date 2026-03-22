package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import kotlinx.coroutines.flow.Flow

interface ISelectedChannelRepository {
    suspend fun loadSelectedChannels(listName: String): List<SelectionChannel>
    fun loadSelectedChannelsAsFlow(): Flow<List<SelectionChannel>>
    suspend fun addChannels(listName: String, selectedChannels: List<SelectionChannel>)
}
