package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.parse.ProgramDTO
import com.mvproject.tvprogramguide.data.model.parse.ProgramParseModel
import com.mvproject.tvprogramguide.data.network.NetworkClient
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.usecases.UpdateProgramsUseCase
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

class UpdateProgramsUseCaseTest : FunSpec({

    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var programRepository: IProgramRepository
    lateinit var programDataSource: IProgramDataSource
    lateinit var useCase: UpdateProgramsUseCase

    // Fixed epoch values used across tests — far enough in the future that end > currentDate
    // currentDate is mocked to 1_000_000L; start/end are well above that.
    val currentTime = 1_000_000L
    val startEpoch = 2_000_000L
    val endEpoch = 3_000_000L

    // Arbitrary parse strings; their actual content doesn't matter because
    // TimeUtils.parseToInstant is mocked for every test.
    val startStr = "202401011000"
    val stopStr = "202401011100"

    beforeTest {
        preferenceRepository = mockk()
        programRepository = mockk()
        programDataSource = mockk()
        useCase = UpdateProgramsUseCase(
            preferenceRepository = preferenceRepository,
            programRepository = programRepository,
            programDataSource = programDataSource,
        )
    }

    afterTest {
        unmockkAll()
    }

    context("invoke") {
        test("programs for a single channel are batched and flushed via the last-batch path") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            every { TimeUtils.parseToInstant(startStr) } returns startEpoch
            every { TimeUtils.parseToInstant(stopStr) } returns endEpoch

            val programmes = listOf(
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch1",
                    title = "Show A"
                ),
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch1",
                    title = "Show B"
                ),
            )

            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } coAnswers {
                val callback = arg<suspend (ProgramParseModel) -> Unit>(1)
                programmes.forEach { callback(it) }
            }

            val expectedDtos = listOf(
                ProgramDTO(
                    dateTimeStart = startEpoch,
                    dateTimeEnd = endEpoch,
                    title = "Show A",
                    description = ""
                ),
                ProgramDTO(
                    dateTimeStart = startEpoch,
                    dateTimeEnd = endEpoch,
                    title = "Show B",
                    description = ""
                ),
            )

            coEvery {
                programRepository.updatePrograms(channelId = "ch1", programs = expectedDtos)
            } just Runs
            coEvery { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) } just Runs
            coEvery { preferenceRepository.setProgramsUpdateRequiredState(false) } just Runs

            var nextIdCount = 0
            useCase { nextIdCount++ }

            // The single channel's batch is flushed via the last-batch path (no channel change occurs)
            coVerify(exactly = 1) {
                programRepository.updatePrograms(channelId = "ch1", programs = expectedDtos)
            }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateRequiredState(false) }
            // onNextId called once per new channel encountered
            assert(nextIdCount == 1) { "Expected onNextId to be called once, was $nextIdCount" }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }

        test("programs split across two channels trigger updatePrograms for each channel") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            every { TimeUtils.parseToInstant(startStr) } returns startEpoch
            every { TimeUtils.parseToInstant(stopStr) } returns endEpoch

            val programmes = listOf(
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch1",
                    title = "Show A"
                ),
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch2",
                    title = "Show B"
                ),
            )

            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } coAnswers {
                val callback = arg<suspend (ProgramParseModel) -> Unit>(1)
                programmes.forEach { callback(it) }
            }

            val dtoA = listOf(
                ProgramDTO(
                    dateTimeStart = startEpoch,
                    dateTimeEnd = endEpoch,
                    title = "Show A",
                    description = ""
                )
            )
            val dtoB = listOf(
                ProgramDTO(
                    dateTimeStart = startEpoch,
                    dateTimeEnd = endEpoch,
                    title = "Show B",
                    description = ""
                )
            )

            coEvery {
                programRepository.updatePrograms(
                    channelId = "ch1",
                    programs = dtoA
                )
            } just Runs
            coEvery {
                programRepository.updatePrograms(
                    channelId = "ch2",
                    programs = dtoB
                )
            } just Runs
            coEvery { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) } just Runs
            coEvery { preferenceRepository.setProgramsUpdateRequiredState(false) } just Runs

            var nextIdCount = 0
            useCase { nextIdCount++ }

            // ch1 flushed on channel-change; ch2 flushed via last-batch path
            coVerify(exactly = 1) {
                programRepository.updatePrograms(
                    channelId = "ch1",
                    programs = dtoA
                )
            }
            coVerify(exactly = 1) {
                programRepository.updatePrograms(
                    channelId = "ch2",
                    programs = dtoB
                )
            }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateRequiredState(false) }
            assert(nextIdCount == 2) { "Expected onNextId to be called twice, was $nextIdCount" }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }

        test("programs whose end time is not after currentDate are skipped") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            // endEpoch <= currentTime → program is stale and must be skipped
            val staleEnd = currentTime - 1
            every { TimeUtils.parseToInstant(startStr) } returns startEpoch
            every { TimeUtils.parseToInstant(stopStr) } returns staleEnd

            val programmes = listOf(
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch1",
                    title = "Old Show"
                ),
            )

            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } coAnswers {
                val callback = arg<suspend (ProgramParseModel) -> Unit>(1)
                programmes.forEach { callback(it) }
            }

            useCase {}

            // No programs counted → repository and prefs never updated
            coVerify(exactly = 0) { programRepository.updatePrograms(any(), any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsUpdateLastTime(any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsUpdateRequiredState(any()) }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }

        test("malformed programme records are skipped without stopping processing of subsequent records") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime
            // First parse call throws → malformed record skipped
            every { TimeUtils.parseToInstant("BADSTART") } throws IllegalArgumentException("bad format")
            every { TimeUtils.parseToInstant(startStr) } returns startEpoch
            every { TimeUtils.parseToInstant(stopStr) } returns endEpoch

            val programmes = listOf(
                // malformed — parseToInstant will throw
                ProgramParseModel(
                    start = "BADSTART",
                    stop = stopStr,
                    channel = "ch1",
                    title = "Bad Show"
                ),
                // valid
                ProgramParseModel(
                    start = startStr,
                    stop = stopStr,
                    channel = "ch1",
                    title = "Good Show"
                ),
            )

            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } coAnswers {
                val callback = arg<suspend (ProgramParseModel) -> Unit>(1)
                programmes.forEach { callback(it) }
            }

            val expectedDtos = listOf(
                ProgramDTO(
                    dateTimeStart = startEpoch,
                    dateTimeEnd = endEpoch,
                    title = "Good Show",
                    description = ""
                ),
            )
            coEvery {
                programRepository.updatePrograms(
                    channelId = "ch1",
                    programs = expectedDtos
                )
            } just Runs
            coEvery { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) } just Runs
            coEvery { preferenceRepository.setProgramsUpdateRequiredState(false) } just Runs

            var nextIdCount = 0
            useCase { nextIdCount++ }

            coVerify(exactly = 1) {
                programRepository.updatePrograms(
                    channelId = "ch1",
                    programs = expectedDtos
                )
            }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateLastTime(timeInMillis = currentTime) }
            coVerify(exactly = 1) { preferenceRepository.setProgramsUpdateRequiredState(false) }
            // onNextId called once per channel that has valid programs
            assert(nextIdCount == 1) { "Expected onNextId to be called once, was $nextIdCount" }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }

        test("when no valid programmes are parsed, preference updates are NOT called") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime

            // datasource produces no callbacks at all
            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } just Runs

            var nextIdCount = 0
            useCase { nextIdCount++ }

            coVerify(exactly = 0) { programRepository.updatePrograms(any(), any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsUpdateLastTime(any()) }
            coVerify(exactly = 0) { preferenceRepository.setProgramsUpdateRequiredState(any()) }
            // onNextId not called when no valid programs are parsed
            assert(nextIdCount == 0) { "Expected onNextId not to be called, was $nextIdCount" }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }

        test("url passed to downloadAndParseXml matches EPG_FILE2 constant") {
            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns currentTime

            coEvery {
                programDataSource.downloadAndParseXml(url = any(), onProgrammeParsed = any())
            } just Runs

            var nextIdCount = 0
            useCase { nextIdCount++ }

            coVerify(exactly = 1) {
                programDataSource.downloadAndParseXml(
                    url = NetworkClient.EPG_FILE2,
                    onProgrammeParsed = any(),
                )
            }
            // onNextId not called when no valid programs are parsed
            assert(nextIdCount == 0) { "Expected onNextId not to be called, was $nextIdCount" }
            confirmVerified(preferenceRepository, programRepository, programDataSource)
        }
    }
})
