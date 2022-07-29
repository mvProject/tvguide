package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsFromEntities
import com.mvproject.tvprogramguide.data.utils.Mappers.toSelectedChannelWithPrograms
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SortedProgramsUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val preferenceRepository: PreferenceRepository
) {

    private val currentChannelList
        get() = runBlocking { preferenceRepository.loadDefaultUserList().first() }

    suspend fun retrieveSelectedChannelWithPrograms(): List<SelectedChannelWithPrograms> {
        val currentChannelList = preferenceRepository.loadDefaultUserList().first()

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
                itemsCount = preferenceRepository
                    .loadAppSettings()
                    .first()
                    .programsViewCount
            )
    }
}
