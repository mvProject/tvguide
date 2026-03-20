package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.ProgramUtils.toSelectedChannelWithPrograms
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Use case for retrieving selected channels with their associated programs.
 *
 * @property selectedChannelRepository The repository for accessing selected channel data.
 * @property programRepository The repository for accessing program data.
 * @property preferenceRepository The repository for accessing user preferences.
 */
class SelectedChannelsWithPrograms(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val programRepository: ProgramRepository,
    private val preferenceRepository: PreferenceRepository
) {
    /**
     * Retrieves a flow of selected channels with their associated programs.
     *
     * This function performs the following steps:
     * 1. Combines the flows of selected channels and app settings.
     * 2. Extracts the program IDs of selected channels.
     * 3. Loads programs for the selected channels.
     * 4. Transforms the data into a list of SelectedChannelWithPrograms objects.
     *
     * @return A Flow of List<SelectedChannelWithPrograms> representing selected channels with their programs.
     */
    operator fun invoke(): Flow<List<SelectedChannelWithPrograms>> {
        return combine(
            selectedChannelRepository.loadSelectedChannelsAsFlow(),
            preferenceRepository.loadAppSettings()
        ) { selectedChannels, settings ->

            val isBrokenChannelsExists =
                selectedChannels.any { it.channelName.isBlank() && it.channelIcon.isBlank() }

            val actualChannels = if (isBrokenChannelsExists)
                selectedChannels.filter { it.channelName.isNotBlank() && it.channelIcon.isNotBlank() }
            else selectedChannels

            // Extract program IDs of selected channels
            val selectedChannelIds =
                actualChannels.map { item -> item.programId }

            val programsWithChannels =
                programRepository.loadProgramsForChannels(channelsIds = selectedChannelIds)

            if (isBrokenChannelsExists) {
                val parentList = actualChannels.firstOrNull()?.parentList ?: String.empty
                selectedChannelRepository.addChannels(
                    listName = parentList,
                    selectedChannels = actualChannels
                )
            }

            // Transform data into SelectedChannelWithPrograms objects
            programsWithChannels.toSelectedChannelWithPrograms(
                alreadySelected = actualChannels,
                itemsCount = settings.programsViewCount,
            )
        }
    }
}
