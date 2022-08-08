package com.mvproject.tvprogramguide.domain.usecases

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsEntitiesFromChannels
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsFromEntities
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SelectedChannelUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val preferenceRepository: PreferenceRepository
) {
    private val targetList
        get() = runBlocking { preferenceRepository.targetList.first() }

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
        updateChannelsOrdersAfterDelete()
    }

    @Transaction
    suspend fun updateChannelsOrdersAfterDelete() {
        val selectedChannels = selectedChannelRepository
            .loadSelectedChannels(listName = targetList)
            .sortedBy { entity ->
                entity.order
            }
            .updateOrdersByAsc()

        selectedChannelRepository.updateChannels(channels = selectedChannels)
    }

    @Transaction
    suspend fun updateChannelsOrdersAfterReorder(reorderedChannels: List<SelectedChannel>) {
        val reorderedChannelsUpdate = reorderedChannels
            .asSelectedChannelsEntitiesFromChannels()
            .updateOrdersByAsc()

        selectedChannelRepository.updateChannels(channels = reorderedChannelsUpdate)
    }

    private fun List<SelectedChannelEntity>.updateOrdersByAsc(): List<SelectedChannelEntity> {
        return this.mapIndexed { ind, entity ->
            entity.copy(order = ind + 1)
        }
    }
}