package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class UpdateChannelsInfoUseCase(
    private val allChannelRepository: AllChannelRepository,
    private val preferenceRepository: PreferenceRepository,
    private val programDataSource: ProgramDataSource,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            Timber.w("testing UpdateChannelsInfoUseCase invoke")
            val networkChannels = buildList {
                programDataSource.downloadAndParseChannels(
                    url = "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz",
                ) { channel ->
                    add(
                        AvailableChannelResponse(
                            channelName = channel.name,
                            channelId = channel.id,
                            channelIcon = channel.logo,
                        ),
                    )
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
