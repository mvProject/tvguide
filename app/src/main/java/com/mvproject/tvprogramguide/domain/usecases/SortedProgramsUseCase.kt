package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsFromEntities
import com.mvproject.tvprogramguide.data.utils.Mappers.toSelectedChannelWithPrograms
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import javax.inject.Inject

class SortedProgramsUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val storeHelper: StoreHelper
) {
    private val currentChannelList
        get() = storeHelper.defaultChannelList

    suspend fun retrieveSelectedChannelWithPrograms(): List<SelectedChannelWithPrograms> {
        val selectedChannels =
            selectedChannelRepository
                .loadSelectedChannels(listName = currentChannelList)
                .asSelectedChannelsFromEntities()

        val selectedChannelIds = selectedChannels.map { channel ->
            channel.channelId
        }

        val programsWithChannels =
            channelProgramRepository.loadProgramsForChannels(channelsIds = selectedChannelIds)

        return programsWithChannels
            .toSelectedChannelWithPrograms(
                alreadySelected = selectedChannels,
                itemsCount = storeHelper.programByChannelDefaultCount
            )
    }
}
