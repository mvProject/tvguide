package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import com.mvproject.tvprogramguide.utils.TimeUtils.parseToInstant
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case for updating TV program information.
 *
 * @property preferenceRepository The repository for managing user preferences.
 * @property programRepository The repository for managing program data.
 * @property programDataSource The data source for downloading and parsing program data.
 */
class UpdateProgramsUseCase
@Inject constructor(
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
     * @param channelId The ID of the channel to update programs for (currently unused).
     */
    suspend operator fun invoke(channelId: String) {
        /*  withContext(Dispatchers.IO) {

              val parsedTable = loadElements(
                  sourceUrl = "https://epg.ott-play.com/php/show_prog.php?f=edem/epg/$channelId.json",
              )

              val programs = parsedTable.map { element ->
                  val (date, title, description) = element.parseElementDataAsProgram()

                  val (start, end) = parseDateTime(input = date)

                  ProgramDTO(
                      dateTimeStart = start,
                      dateTimeEnd = end,
                      title = title,
                      description = description,
                  )
              }

              Timber.w("testing UpdateChannelsInfoUseCase programs ${programs.count()}")
              if (programs.isNotEmpty()) {
                  channelProgramRepository.updatePrograms(
                      channelId = channelId,
                      programs = programs,
                  )
              }

              preferenceRepository.setChannelsUpdateLastTime(timeInMillis = actualDate)
          }*/


        var programmeCount = 0
        val programsDto = mutableListOf<ProgramDTO>()
        var currentId = String.empty

        programDataSource.downloadAndParseXml("epg2.xml.gz") { programme ->
            programmeCount++
            val dto = ProgramDTO(
                dateTimeStart = parseToInstant(programme.start),
                dateTimeEnd = parseToInstant(programme.stop),
                title = programme.title,
                description = programme.desc ?: String.empty,
            )
            // Group programs by channel and update repository
            if (currentId.isBlank()) {
                currentId = programme.channel
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
                    programsDto.add(dto)
                }
            }
            if (programmeCount % 10000 == 0) {
                Timber.d("testing Parsed $programmeCount programmes")
            }
        }

        Timber.w("testing Finished parsing. Total programmes: $programmeCount")
        preferenceRepository.apply {
            setProgramsUpdateLastTime(timeInMillis = actualDate)
            setProgramsUpdateRequiredState(false)
        }
    }
}
