package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.ProgramUtils.toSelectedChannelWithPrograms
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case to retrieve available channels
 * @property selectedChannelRepository the SelectedChannelRepository repository
 * @property programRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class SelectedChannelsWithPrograms
@Inject
constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val programRepository: ProgramRepository,
    private val preferenceRepository: PreferenceRepository
) {
    /**
     * Obtain list of programs for selected channels and sorted by channels and view count
     *
     * @return sorted list of programs
     */

    operator fun invoke(): Flow<List<SelectedChannelWithPrograms>> {
        return combine(
            selectedChannelRepository.loadSelectedChannelsAsFlow(),
            preferenceRepository.loadAppSettings()
        ) { selectedChannels, settings ->

            val selectedChannelIds =
                selectedChannels.map { item -> item.programId }

            val programsWithChannels =
                programRepository.loadProgramsForChannels(channelsIds = selectedChannelIds)

            programsWithChannels.toSelectedChannelWithPrograms(
                alreadySelected = selectedChannels,
                itemsCount = settings.programsViewCount,
            )
        }
    }
}
