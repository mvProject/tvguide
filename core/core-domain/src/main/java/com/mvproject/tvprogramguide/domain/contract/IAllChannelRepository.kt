package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse

interface IAllChannelRepository {
    suspend fun loadChannels(): List<SelectionChannel>
    suspend fun loadChannelsById(selectedIds: List<String>): List<SelectionChannel>
    suspend fun updateChannels(channels: List<AvailableChannelResponse>)
}
