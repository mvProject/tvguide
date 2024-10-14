package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import com.mvproject.tvprogramguide.utils.TimeUtils.parseToInstant
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case to update available channels
 * @property channelProgramRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class UpdateProgramsUseCase
@Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val programDataSource: ProgramDataSource,
) {
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

            if (currentId.isBlank()) {
                currentId = programme.channel
                programsDto.add(dto)
            } else {
                if (programme.channel == currentId) {
                    programsDto.add(dto)
                } else {
                    if (programsDto.isNotEmpty()) {
                        channelProgramRepository.updatePrograms(
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
