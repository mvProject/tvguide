package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.ProgramUtils.toSelectedChannelWithPrograms
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Use case for retrieving selected channels with their associated programs.
 *
 * @property selectedChannelRepository The repository for accessing selected channel data.
 * @property programRepository The repository for accessing program data.
 * @property preferenceRepository The repository for accessing user preferences.
 */
class GetSelectedChannelsWithProgramsUseCase(
    private val selectedChannelRepository: ISelectedChannelRepository,
    private val programRepository: IProgramRepository,
    private val preferenceRepository: IPreferenceRepository
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
        return selectedChannelRepository.loadSelectedChannelsAsFlow()
            .map { selectedChannels ->
                // Single pass: partition into broken and valid channels
                selectedChannels.partition { it.channelName.isBlank() && it.channelIcon.isBlank() }
            }
            .onEach { (broken, valid) ->
                // Side effect kept out of the transform: write only when cleanup is needed.
                // The resulting DB emission will have no broken channels, so this runs at most once.
                if (broken.isNotEmpty()) {
                    val parentList = valid.firstOrNull()?.parentList ?: String.empty
                    selectedChannelRepository.addChannels(
                        listName = parentList,
                        selectedChannels = valid,
                    )
                }
            }
            .map { (_, valid) -> valid }
            .distinctUntilChanged()
            .flatMapLatest { actualChannels ->
                // DB query only runs when channels change, not on every settings change
                val ids = actualChannels.map { it.programId }
                val programs = programRepository.loadProgramsForChannels(channelsIds = ids)
                preferenceRepository.loadAppSettings().map { settings ->
                    programs.toSelectedChannelWithPrograms(
                        alreadySelected = actualChannels,
                        itemsCount = settings.programsViewCount,
                    )
                }
            }
    }
}
