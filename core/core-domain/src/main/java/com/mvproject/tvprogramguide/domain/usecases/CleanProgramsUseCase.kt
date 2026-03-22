package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import kotlin.time.Duration.Companion.days

/**
 * Use case to clean up old program data.
 *
 * @property programRepository The repository for managing program data.
 * @property preferenceRepository The repository for managing app preferences.
 */
class CleanProgramsUseCase(
    private val preferenceRepository: IPreferenceRepository,
    private val programRepository: IProgramRepository,
) {
    /**
     * Executes the program cleanup process.
     *
     * This function performs the following steps:
     * 1. Checks if it's time to perform a cleanup (once per day).
     * 2. If cleanup is needed, removes old program data.
     * 3. Updates the last cleanup time in preferences.
     *
     * The cleanup process is dispatcher-agnostic; the caller is responsible for the coroutine context.
     */
    suspend operator fun invoke() {
        val current = TimeUtils.actualDate
        val lastCleanup = preferenceRepository.getProgramsCleanTime()
        // Check if at least one day has passed since the last cleanup
        if ((current - lastCleanup) > 1.days.inWholeMilliseconds) {
            // Clean up programs with dates before the current date
            programRepository.cleanProgramsBeforeDate(date = current)
            // Update the last cleanup time
            preferenceRepository.setProgramsCleanTime(timeInMillis = current)
        }
    }
}
