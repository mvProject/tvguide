package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.domain.constants.NetworkConstants
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import timber.log.Timber

/**
 * Use case for downloading and updating channel information from the remote source.
 *
 * @property allChannelRepository The repository for storing all available channels.
 * @property preferenceRepository The repository for managing app preferences.
 * @property programDataSource The data source for downloading channel data.
 */
class UpdateChannelsInfoUseCase(
    private val allChannelRepository: IAllChannelRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val programDataSource: IProgramDataSource,
) {
    suspend operator fun invoke() {
        Timber.d("UpdateChannelsInfoUseCase invoke")
        val networkChannels = buildList {
            programDataSource.downloadAndParseChannels(
                url = NetworkConstants.EPG_CHANNELS_URL,
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

        Timber.d("UpdateChannelsInfoUseCase networkChannels ${networkChannels.count()}")
        if (networkChannels.isNotEmpty()) {
            allChannelRepository.updateChannels(channels = networkChannels)
        }

        preferenceRepository.setChannelsUpdateLastTime(timeInMillis = actualDate)
    }
}
