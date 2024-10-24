package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.convertDateToReadableFormat
import javax.inject.Inject

/**
 * Use case for retrieving and organizing programs for a specific channel.
 *
 * @property programRepository The repository for accessing program data.
 */
class GetProgramsByChannel
@Inject
constructor(
    private val programRepository: ProgramRepository,
) {
    /**
     * Retrieves and organizes programs for a specified channel, grouped by date.
     *
     * This function performs the following steps:
     * 1. Loads all programs for the specified channel from the programRepository.
     * 2. Groups the programs by their start date.
     * 3. Creates a list of SingleChannelWithPrograms objects, each representing a day's programs.
     *
     * @param channelId The ID of the channel to retrieve programs for.
     * @return A list of SingleChannelWithPrograms objects, each containing a date and its programs.
     */
    suspend operator fun invoke(channelId: String): List<SingleChannelWithPrograms> {
        val programs =
            programRepository
                .loadProgramsForChannel(channelId = channelId)
        // Group programs by their start date
        val programsByDays =
            programs.groupBy { program ->
                program.dateTimeStart.convertDateToReadableFormat()
            }

        //return buildList {
        return programsByDays.map { (day, data) ->
            //add(
            SingleChannelWithPrograms(
                date = day,
                programs = data,
            )
            // )
        }
        // }
    }
}
