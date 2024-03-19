package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.asSelectedChannelsFromAltEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toSelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case to retrieve available channels
 * @property selectedChannelRepository the SelectedChannelRepository repository
 * @property channelProgramRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class SelectedChannelsWithPrograms
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository,
        private val channelProgramRepository: ChannelProgramRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        /**
         * Obtain list of programs for selected channels and sorted by channels and view count
         *
         * @return sorted list of programs
         */
        suspend operator fun invoke(): List<SelectedChannelWithPrograms> {
            val currentChannelList = preferenceRepository.loadDefaultUserList().first()
            val itemsCount =
                preferenceRepository
                    .loadAppSettings()
                    .first()
                    .programsViewCount

            val selectedChannels =
                selectedChannelRepository
                    .loadSelectedChannels(listName = currentChannelList)
                    .asSelectedChannelsFromAltEntities()

            val selectedChannelIds =
                selectedChannels.map { channel ->
                    channel.channelId
                }

            val programsWithChannels =
                channelProgramRepository.loadProgramsForChannels(channelsIds = selectedChannelIds)

            return programsWithChannels.toSelectedChannelWithPrograms(
                alreadySelected = selectedChannels,
                itemsCount = itemsCount,
            )
        }
    }
