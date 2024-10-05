package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.response.ProgramResponse2
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.ParserUtils.loadElements
import com.mvproject.tvprogramguide.utils.ParserUtils.parseElementDataAsProgram
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import com.mvproject.tvprogramguide.utils.TimeUtils.parseDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case to update available channels
 * @property allChannelRepository the AllChannelRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class UpdateProgramsUseCase
    @Inject
    constructor(
        private val allChannelRepository: AllChannelRepository,
        private val preferenceRepository: PreferenceRepository,
        private val channelProgramRepository: ChannelProgramRepository,
    ) {
        suspend operator fun invoke(channelId: String) {
            val parsedTable =
                loadElements(
                    sourceUrl = "https://epg.ott-play.com/php/show_prog.php?f=edem/epg/$channelId.json",
                )

            val programs =
                buildList {
                    parsedTable.forEach { element ->
                        val (date, title, description) = element.parseElementDataAsProgram()

                        val (start, end) = parseDateTime(input = date)

                        val program =
                            ProgramResponse2(
                                dateTimeStart = start,
                                dateTimeEnd = end,
                                title = title,
                                description = description,
                            )

                        add(program)
                    }
                }

            Timber.w("testing UpdateChannelsInfoUseCase programs ${programs.count()}")
            if (programs.isNotEmpty()) {
                channelProgramRepository.refreshPrograms(
                    channelId = channelId,
                    programs = programs,
                )
            }

            preferenceRepository.setChannelsUpdateLastTime(timeInMillis = actualDate)
        }
    }
