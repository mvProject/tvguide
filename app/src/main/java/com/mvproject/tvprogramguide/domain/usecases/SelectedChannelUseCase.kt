package com.mvproject.tvprogramguide.domain.usecases

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectedChannelsEntitiesFromChannels
import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectedChannelsFromAltEntities
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Use case to handle user lists and channels
 * @property selectedChannelRepository the selectedChannelRepository repository
 * @property preferenceRepository the preferences repository
 */
class SelectedChannelUseCase
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        /**
         * Obtain current operating user list
         *
         * @return current user list
         */
        private val targetList
            get() = runBlocking { preferenceRepository.targetList.first() }

        /**
         * Obtain channels add to user list
         *
         * @return list of channels for userlist
         */
        fun loadSelectedChannelsFlow() =
            selectedChannelRepository.loadSelectedChannelsFlow(listName = targetList)
                .map { entities ->
                    entities.asSelectedChannelsFromAltEntities()
                }

        /**
         * Add new channel to selected
         *
         * @param selectedChannel channel model to add
         */
        suspend fun addChannelToSelected(selectedChannel: AvailableChannel) {
            val currentCount =
                selectedChannelRepository
                    .loadSelectedChannels(listName = targetList)
                    .count() + COUNT_ONE

            val selected =
                SelectedChannelEntity(
                    channelId = selectedChannel.channelId,
                    channelName = selectedChannel.channelName,
                    order = currentCount,
                    parentList = targetList,
                )
            selectedChannelRepository.addChannel(selectedChannel = selected)
            // preferenceRepository.setChannelsCountChanged(true)
        }

        /**
         * Delete channel from selected
         *
         * @param channelId the id for delete
         */
        suspend fun deleteChannelFromSelected(channelId: String) {
            selectedChannelRepository.deleteChannel(channelId = channelId)
            updateChannelsOrdersAfterDelete()
        }

        /**
         * Update orders and save entities after deleting entity
         */
        @Transaction
        private suspend fun updateChannelsOrdersAfterDelete() {
            val selectedChannels =
                selectedChannelRepository
                    .loadSelectedChannels(listName = targetList)
                    .sortedBy { entity ->
                        entity.channel.order
                    }
                    .map { entity ->
                        entity.channel
                    }
                    .updateOrdersByAsc()

            selectedChannelRepository.updateChannels(channels = selectedChannels)
        }

        /**
         * Update and save entities after reordering
         *
         * @param reorderedChannels list of reordered entities
         */
        @Transaction
        suspend fun updateChannelsOrdersAfterReorder(reorderedChannels: List<SelectedChannel>) {
            val reorderedChannelsUpdate =
                reorderedChannels
                    .asSelectedChannelsEntitiesFromChannels()
                    .updateOrdersByAsc()

            selectedChannelRepository.updateChannels(channels = reorderedChannelsUpdate)
        }

        /**
         * Extension for update order property according entities index in list.
         *
         * @return sorted entities list
         */
        private fun List<SelectedChannelEntity>.updateOrdersByAsc(): List<SelectedChannelEntity> {
            return this.mapIndexed { ind, entity ->
                entity.copy(order = ind + 1)
            }
        }
    }
