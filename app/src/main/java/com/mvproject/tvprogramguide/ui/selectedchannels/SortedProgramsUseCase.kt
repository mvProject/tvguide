package com.mvproject.tvprogramguide.ui.selectedchannels

import com.mvproject.tvprogramguide.data.model.SelectedChannelModel
import com.mvproject.tvprogramguide.domain.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.domain.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.utils.Mappers.toSortedSelectedChannelsPrograms
import com.mvproject.tvprogramguide.helpers.StoreHelper
import javax.inject.Inject

class SortedProgramsUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val storeHelper: StoreHelper
) {

    suspend operator fun invoke(listName: String): List<SelectedChannelModel> {
        val selectedChannels =
            selectedChannelRepository.loadSelectedChannels(listName)

        val selectedChannelIds = selectedChannels.map { it.channelId }
        val programsWithChannels =
            channelProgramRepository.loadPrograms(selectedChannelIds)

        return programsWithChannels
            .toSortedSelectedChannelsPrograms(
                selectedChannels,
                storeHelper.programByChannelDefaultCount
            )
    }
}
