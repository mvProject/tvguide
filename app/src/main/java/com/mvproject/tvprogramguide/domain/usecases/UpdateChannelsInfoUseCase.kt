package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.ParserUtils.loadElements
import com.mvproject.tvprogramguide.utils.ParserUtils.parseElementDataAsChannel
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case for updating available channels information.
 *
 * @property allChannelRepository The repository for managing all channel data.
 * @property preferenceRepository The repository for managing user preferences.
 */
class UpdateChannelsInfoUseCase
@Inject constructor(
    private val allChannelRepository: AllChannelRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    /**
     * Updates the available channels information from a remote source.
     *
     * This function performs the following steps:
     * 1. Loads channel data from a remote XML source.
     * 2. Parses the loaded data into AvailableChannelResponse objects.
     * 3. Updates the local database with the new channel information.
     * 4. Updates the last update time in the preferences.
     *
     * The function runs on the IO dispatcher to prevent blocking the main thread.
     */
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val parsedTable = loadElements(
                sourceUrl = "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz",
            )
            // Parse loaded data into AvailableChannelResponse objects
            val networkChannels = buildList {
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
}
