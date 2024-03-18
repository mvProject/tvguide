package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.AppConstants
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case to update available channels
 * @property selectedChannelRepository the SelectedChannelRepository repository
 * @property channelProgramRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class UpdateProgramsUseCase
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository,
        private val channelProgramRepository: ChannelProgramRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        suspend operator fun invoke() {
            val selectedChannels = selectedChannelRepository.loadSelectedChannelsIds()

            val channelsCount = selectedChannels.count()
            if (channelsCount > AppConstants.COUNT_ZERO) {
                selectedChannels.forEach { chn ->
                    channelProgramRepository.loadProgram(channelId = chn)
                }
                preferenceRepository.apply {
                    setProgramsUpdateLastTime(timeInMillis = actualDate)
                    setChannelsCountChanged(false)
                    setChannelsUpdateRequired(false)
                }
            } else {
                Timber.e("FullUpdateProgramsWorker update count zero")
            }
        }
    }
