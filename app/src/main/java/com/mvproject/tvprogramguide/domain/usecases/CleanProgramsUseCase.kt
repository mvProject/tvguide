package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

/**
 * Use case to update available channels
 * @property channelProgramRepository the ChannelProgramRepository repository
 * @property preferenceRepository the PreferenceRepository repository
 */
class CleanProgramsUseCase
@Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val channelProgramRepository: ChannelProgramRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = Clock.System.now().toEpochMilliseconds()
            val lastCleanup = preferenceRepository.getProgramsCleanTime()
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds) {
                val programs = channelProgramRepository.loadPrograms()
                val filtered = programs.filter { program ->
                    program.dateTimeEnd > current
                }
                channelProgramRepository.cleanPrograms(programs = filtered)
                preferenceRepository.setProgramsCleanTime(timeInMillis = current)
            }
        }
    }
}
