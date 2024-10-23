package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

/**
 * Use case to update available channels
 * @property programRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class CleanProgramsUseCase
@Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val programRepository: ProgramRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = TimeUtils.actualDate
            val lastCleanup = preferenceRepository.getProgramsCleanTime()
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds) {
                programRepository.cleanProgramsBeforeDate(date = current)
                preferenceRepository.setProgramsCleanTime(timeInMillis = current)
            }
        }
    }
}
