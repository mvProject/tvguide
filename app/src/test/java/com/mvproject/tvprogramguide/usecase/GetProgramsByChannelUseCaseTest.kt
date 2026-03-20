package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannelUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Helper to produce a Long timestamp that formats to a known date string under
 * convertDateToReadableFormat ("dd MM yyyy" via SimpleDateFormat with Locale.getDefault()).
 * We generate fixed epoch millis for two distinct calendar dates.
 */
private fun timestampForDateString(dateString: String): Long =
    SimpleDateFormat("dd MM yyyy", Locale.getDefault()).parse(dateString)!!.time

class GetProgramsByChannelUseCaseTest : FunSpec({

    lateinit var programRepository: IProgramRepository
    lateinit var useCase: GetProgramsByChannelUseCase

    beforeTest {
        programRepository = mockk()
        useCase = GetProgramsByChannelUseCase(programRepository)
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("programs with the same date are grouped into the same SingleChannelWithPrograms") {
            val channelId = "channel_1"
            val date1 = "20 03 2026"
            val date2 = "21 03 2026"

            val ts1a = timestampForDateString(date1)
            val ts1b = timestampForDateString(date1) + 3_600_000L   // +1 hour, still same day
            val ts2a = timestampForDateString(date2)

            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = ts1a,
                    dateTimeEnd = ts1a + 1800_000L,
                    channel = channelId
                ),
                Program(
                    programId = "p2",
                    dateTimeStart = ts1b,
                    dateTimeEnd = ts1b + 1800_000L,
                    channel = channelId
                ),
                Program(
                    programId = "p3",
                    dateTimeStart = ts2a,
                    dateTimeEnd = ts2a + 1800_000L,
                    channel = channelId
                ),
            )

            coEvery { programRepository.loadProgramsForChannel(channelId = channelId) } returns programs

            val result = useCase(channelId)

            result.size shouldBe 2

            val dates = result.map { it.date }
            dates shouldContainExactlyInAnyOrder listOf(date1, date2)

            coVerify(exactly = 1) { programRepository.loadProgramsForChannel(channelId = channelId) }
            confirmVerified(programRepository)
        }

        test("each group contains only the programs belonging to that date") {
            val channelId = "channel_2"
            val date1 = "20 03 2026"
            val date2 = "21 03 2026"

            val ts1a = timestampForDateString(date1)
            val ts1b = timestampForDateString(date1) + 7_200_000L   // +2 hours, still day 1
            val ts2a = timestampForDateString(date2)

            val p1 = Program(
                programId = "p1",
                dateTimeStart = ts1a,
                dateTimeEnd = ts1a + 1800_000L,
                channel = channelId
            )
            val p2 = Program(
                programId = "p2",
                dateTimeStart = ts1b,
                dateTimeEnd = ts1b + 1800_000L,
                channel = channelId
            )
            val p3 = Program(
                programId = "p3",
                dateTimeStart = ts2a,
                dateTimeEnd = ts2a + 1800_000L,
                channel = channelId
            )

            coEvery { programRepository.loadProgramsForChannel(channelId = channelId) } returns listOf(
                p1,
                p2,
                p3
            )

            val result = useCase(channelId)

            val group1 = result.first { it.date == date1 }
            val group2 = result.first { it.date == date2 }

            group1.programs.map { it.programId } shouldContainExactlyInAnyOrder listOf("p1", "p2")
            group2.programs.map { it.programId } shouldContainExactlyInAnyOrder listOf("p3")

            coVerify(exactly = 1) { programRepository.loadProgramsForChannel(channelId = channelId) }
            confirmVerified(programRepository)
        }

        test("when repository returns empty list, result is empty") {
            val channelId = "channel_empty"

            coEvery { programRepository.loadProgramsForChannel(channelId = channelId) } returns emptyList()

            val result = useCase(channelId)

            result shouldBe emptyList()

            coVerify(exactly = 1) { programRepository.loadProgramsForChannel(channelId = channelId) }
            confirmVerified(programRepository)
        }

        test("when all programs are on the same date, result has a single group") {
            val channelId = "channel_3"
            val date1 = "20 03 2026"

            val ts = timestampForDateString(date1)
            val programs = listOf(
                Program(
                    programId = "p1",
                    dateTimeStart = ts,
                    dateTimeEnd = ts + 1800_000L,
                    channel = channelId
                ),
                Program(
                    programId = "p2",
                    dateTimeStart = ts + 3_600_000L,
                    dateTimeEnd = ts + 5400_000L,
                    channel = channelId
                ),
                Program(
                    programId = "p3",
                    dateTimeStart = ts + 7_200_000L,
                    dateTimeEnd = ts + 9000_000L,
                    channel = channelId
                ),
            )

            coEvery { programRepository.loadProgramsForChannel(channelId = channelId) } returns programs

            val result = useCase(channelId)

            result.size shouldBe 1
            result.first().date shouldBe date1
            result.first().programs.size shouldBe 3

            coVerify(exactly = 1) { programRepository.loadProgramsForChannel(channelId = channelId) }
            confirmVerified(programRepository)
        }
    }
})
