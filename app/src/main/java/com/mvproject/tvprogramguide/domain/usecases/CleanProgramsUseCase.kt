package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

/**
 * Use case to clean up old program data.
 *
 * @property programRepository The repository for managing program data.
 * @property preferenceRepository The repository for managing app preferences.
 */
class CleanProgramsUseCase
@Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val programRepository: ProgramRepository,
) {
    /**
     * Executes the program cleanup process.
     *
     * This function performs the following steps:
     * 1. Checks if it's time to perform a cleanup (once per day).
     * 2. If cleanup is needed, removes old program data.
     * 3. Updates the last cleanup time in preferences.
     *
     * The cleanup process runs on the IO dispatcher to avoid blocking the main thread.
     */
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
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
}
