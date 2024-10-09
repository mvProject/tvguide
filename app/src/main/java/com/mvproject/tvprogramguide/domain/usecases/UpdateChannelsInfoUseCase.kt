package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.ParserUtils.loadElements
import com.mvproject.tvprogramguide.utils.ParserUtils.parseElementDataAsChannel
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case to update available channels
 * @property allChannelRepository the AllChannelRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class UpdateChannelsInfoUseCase
    @Inject
    constructor(
        private val allChannelRepository: AllChannelRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        suspend operator fun invoke() {
            val parsedTable =
                loadElements(
                    sourceUrl = "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz",
                )

            val networkChannels =
                buildList {
                    parsedTable.forEach { element ->
                        val (id, logo, names) = element.parseElementDataAsChannel()

                        names.forEach { name ->
                            add(
                                AvailableChannelResponse(
                                    channelNames = name,
                                    channelId = id,
                                    channelIcon = logo,
                                ),
                            )
                        }
                    }
                }

            Timber.w("testing UpdateChannelsInfoUseCase networkChannels ${networkChannels.count()}")
            if (networkChannels.isNotEmpty()) {
                allChannelRepository.updateChannels(channels = networkChannels)
            }

            preferenceRepository.setChannelsUpdateLastTime(timeInMillis = actualDate)
        }
    }
