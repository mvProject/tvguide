package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.data.model.parse.ProgramDTO
import com.mvproject.tvprogramguide.data.network.NetworkClient.EPG_FILE2
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.TimeUtils
import com.mvproject.tvprogramguide.utils.TimeUtils.parseToInstant
import timber.log.Timber

/**
 * Use case for updating TV program information.
 *
 * @property preferenceRepository The repository for managing user preferences.
 * @property programRepository The repository for managing program data.
 * @property programDataSource The data source for downloading and parsing program data.
 */
class UpdateProgramsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val programRepository: ProgramRepository,
    private val programDataSource: ProgramDataSource,
) {
    /**
     * Updates the TV program information from a remote source.
     *
     * This function performs the following steps:
     * 1. Downloads and parses XML data containing program information.
     * 2. Processes each program entry, converting it to a ProgramDTO.
     * 3. Groups programs by channel and updates the repository.
     * 4. Updates the last update time and update required state in preferences.
     *
     */
    suspend operator fun invoke(onNextId: () -> Unit) {
        val currentDate = TimeUtils.actualDate
        var programmeCount = 0
        val programsDto = mutableListOf<ProgramDTO>()
        var currentId = String.empty

        programDataSource.downloadAndParseXml(url = EPG_FILE2) { programme ->
            try {
                val start = parseToInstant(programme.start)
                val end = parseToInstant(programme.stop)

                if (end > currentDate) {
                    programmeCount++
                    val dto = ProgramDTO(
                        dateTimeStart = start,
                        dateTimeEnd = end,
                        title = programme.title,
                        description = programme.desc ?: String.empty,
                    )
                    if (currentId.isBlank()) {
                        currentId = programme.channel
                        onNextId()
                        programsDto.add(dto)
                    } else {
                        if (programme.channel == currentId) {
                            programsDto.add(dto)
                        } else {
                            if (programsDto.isNotEmpty()) {
                                programRepository.updatePrograms(
                                    channelId = currentId,
                                    programs = programsDto,
                                )
                            }
                            programsDto.clear()
                            currentId = programme.channel
                            onNextId()
                            programsDto.add(dto)
                        }
                    }
                }
                if (programmeCount > 0 && programmeCount % 10000 == 0) {
                    Timber.d("Parsed $programmeCount programmes")
                }
            } catch (ex: Exception) {
                Timber.w("Skipping malformed record channel=${programme.channel} start=${programme.start}: ${ex.message}")
            }
        }

        // Flush the last channel's batch — it never triggers the channel-change branch above
        if (programsDto.isNotEmpty()) {
            programRepository.updatePrograms(channelId = currentId, programs = programsDto)
        }

        if (programmeCount > 0) {
            Timber.d("Finished parsing. Total programmes: $programmeCount")
            preferenceRepository.apply {
                setProgramsUpdateLastTime(timeInMillis = currentDate)
                setProgramsUpdateRequiredState(false)
            }
        }
    }
}
