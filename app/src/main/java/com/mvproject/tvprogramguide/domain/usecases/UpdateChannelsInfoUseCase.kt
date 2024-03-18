package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.TimeUtils.actualDate
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
            allChannelRepository.loadProgramFromSource()
            preferenceRepository.setChannelsUpdateLastTime(timeInMillis = actualDate)
        }
    }
