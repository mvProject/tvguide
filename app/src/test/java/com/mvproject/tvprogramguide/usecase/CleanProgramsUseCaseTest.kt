package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.usecases.CleanProgramsUseCase
import com.mvproject.tvprogramguide.utils.TimeUtils
import io.kotest.core.spec.style.FunSpec
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlin.time.Duration.Companion.days

class CleanProgramsUseCaseTest : FunSpec({

    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var programRepository: IProgramRepository
    lateinit var useCase: CleanProgramsUseCase

    beforeTest {
        preferenceRepository = mockk()
        programRepository = mockk()
        useCase = CleanProgramsUseCase(preferenceRepository, programRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("when last clean time is more than 1 day ago, cleanProgramsBeforeDate and setProgramsCleanTime are called") {
            val currentTime = 10_000_000L
            // last cleanup was 2 days ago — exceeds the 1-day threshold
            val lastCleanup = currentTime - 2.days.inWholeMilliseconds

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            coEvery { preferenceRepository.getProgramsCleanTime() } returns lastCleanup
            coEvery { programRepository.cleanProgramsBeforeDate(date = currentTime) } just Runs
            coEvery { preferenceRepository.setProgramsCleanTime(timeInMillis = currentTime) } just Runs

            useCase()

            coVerify(exactly = 1) { preferenceRepository.getProgramsCleanTime() }
            coVerify(exactly = 1) { programRepository.cleanProgramsBeforeDate(date = currentTime) }
            coVerify(exactly = 1) { preferenceRepository.setProgramsCleanTime(timeInMillis = currentTime) }
            confirmVerified(preferenceRepository, programRepository)
        }

        test("when last clean time is exactly 1 day ago, cleanProgramsBeforeDate is NOT called") {
            val currentTime = 10_000_000L
            // exactly 1 day ago — not strictly greater than 1 day, so no cleanup
            val lastCleanup = currentTime - 1.days.inWholeMilliseconds

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            coEvery { preferenceRepository.getProgramsCleanTime() } returns lastCleanup

            useCase()

            coVerify(exactly = 1) { preferenceRepository.getProgramsCleanTime() }
            coVerify(exactly = 0) { programRepository.cleanProgramsBeforeDate(any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsCleanTime(any()) }
            confirmVerified(preferenceRepository, programRepository)
        }

        test("when last clean time is less than 1 day ago, cleanProgramsBeforeDate is NOT called") {
            val currentTime = 10_000_000L
            // 12 hours ago — within the 1-day threshold
            val lastCleanup = currentTime - (1.days.inWholeMilliseconds / 2)

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            coEvery { preferenceRepository.getProgramsCleanTime() } returns lastCleanup

            useCase()

            coVerify(exactly = 1) { preferenceRepository.getProgramsCleanTime() }
            coVerify(exactly = 0) { programRepository.cleanProgramsBeforeDate(any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsCleanTime(any()) }
            confirmVerified(preferenceRepository, programRepository)
        }
    }
})
