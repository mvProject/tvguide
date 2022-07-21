package com.mvproject.tvprogramguide.domain.usecases

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsFromEntities
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectedChannelUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val storeHelper: StoreHelper
) {
    private val currentChannelList
        get() = storeHelper.currentChannelList

    private val defaultChannelList
        get() = storeHelper.defaultChannelList

    private val targetList
        get() = currentChannelList.ifEmpty { defaultChannelList }

    suspend fun loadSelectedChannels(): List<SelectedChannel> {
        return selectedChannelRepository.loadSelectedChannels(listName = targetList)
            .asSelectedChannelsFromEntities()
    }

    suspend fun loadSelectedChannelsIds() =
        selectedChannelRepository.loadSelectedChannelsIds()

    fun loadSelectedChannelsFlow() =
        selectedChannelRepository.loadSelectedChannelsFlow(listName = targetList)
            .map { entities ->
                entities.asSelectedChannelsFromEntities()
            }

    suspend fun addChannelToSelected(selectedChannel: AvailableChannel) {
        val currentCount = selectedChannelRepository
            .loadSelectedChannels(listName = targetList)
            .count() + 1

        val selected = SelectedChannelEntity(
            channelId = selectedChannel.channelId,
            channelName = selectedChannel.channelName,
            channelIcon = selectedChannel.channelIcon,
            order = currentCount,
            parentList = targetList
        )
        selectedChannelRepository.addChannel(selectedChannel = selected)
    }

    suspend fun deleteChannelFromSelected(channelId: String) {
        selectedChannelRepository.deleteChannel(channelId = channelId)
        updateChannelsOrdering()
    }

    @Transaction
    suspend fun updateChannelsOrdering() {
        val selectedChannels = selectedChannelRepository
            .loadSelectedChannels(listName = targetList)

        val selectedOrderedChannels = selectedChannels.mapIndexed { ind, entity ->
            entity.copy(order = ind + 1)
        }
        selectedChannelRepository.updateChannels(channels = selectedOrderedChannels)

    }
}