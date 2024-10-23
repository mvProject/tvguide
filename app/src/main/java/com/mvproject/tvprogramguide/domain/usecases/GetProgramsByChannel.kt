package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.convertDateToReadableFormat
import javax.inject.Inject

/**
 * Use case to retrieving and updating sorted programs
 * @property programRepository the channelProgramRepository repository
 */
class GetProgramsByChannel
@Inject
constructor(
    private val programRepository: ProgramRepository,
) {
    /**
     * Obtain list of programs for selected channel and sorted by date
     *
     * @param channelId the alarm id
     * @return sorted list of programs
     */
    suspend operator fun invoke(channelId: String): List<SingleChannelWithPrograms> {
        val programs =
            programRepository
                .loadProgramsForChannel(channelId = channelId)
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
