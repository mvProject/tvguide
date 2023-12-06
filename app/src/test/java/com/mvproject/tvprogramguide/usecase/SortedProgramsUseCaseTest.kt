package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.utils.convertDateToReadableFormat
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalCoroutinesApi::class)
class SortedProgramsUseCaseTest : StringSpec({

    lateinit var preferenceRepository: PreferenceRepository
    lateinit var selectedChannelRepository: SelectedChannelRepository
    lateinit var channelProgramRepository: ChannelProgramRepository
    lateinit var programSchedulerHelper: ProgramSchedulerHelper

    lateinit var sortedProgramUseCase: SortedProgramsUseCase

    beforeTest {
        preferenceRepository = createPreferenceMockRepository()
        selectedChannelRepository = createSelectedChannelMockRepository()
        channelProgramRepository = createChannelProgramMockRepository()
        programSchedulerHelper = createProgramSchedulerHelper()

        sortedProgramUseCase = SortedProgramsUseCase(
            selectedChannelRepository,
            channelProgramRepository,
            preferenceRepository,
            programSchedulerHelper
        )
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    assertSoftly {
        "get sorted programs for single channel" {
            val time = Clock.System.now()

            val expectedPrograms = listOf(
                Program(
                    time.toEpochMilliseconds(),
                    (time + 1.hours).toEpochMilliseconds(),
                    "title 1",
                    channel = "testId1"
                ),
                Program(
                    (time + 1.hours).toEpochMilliseconds(),
                    (time + 2.hours).toEpochMilliseconds(),
                    "title 2",
                    channel = "testId1"
                ),
                Program(
                    (time + 2.hours).toEpochMilliseconds(),
                    (time + 3.hours).toEpochMilliseconds(),
                    "title 3",
                    channel = "testId1"
                ),
                Program(
                    (time + 25.hours).toEpochMilliseconds(),
                    (time + 26.hours).toEpochMilliseconds(),
                    "title 4",
                    channel = "testId1"
                ),
                Program(
                    (time + 26.hours).toEpochMilliseconds(),
                    (time + 27.hours).toEpochMilliseconds(),
                    "title 5",
                    channel = "testId1"
                )
            )

            val expectedResult = listOf(
                SingleChannelWithPrograms(
                    date = time.toEpochMilliseconds().convertDateToReadableFormat(),
                    programs = listOf(
                        Program(
                            time.toEpochMilliseconds(),
                            (time + 1.hours).toEpochMilliseconds(),
                            "title 1",
                            channel = "testId1"
                        ),
                        Program(
                            (time + 1.hours).toEpochMilliseconds(),
                            (time + 2.hours).toEpochMilliseconds(),
                            "title 2",
                            channel = "testId1"
                        ),
                        Program(
                            (time + 2.hours).toEpochMilliseconds(),
                            (time + 3.hours).toEpochMilliseconds(),
                            "title 3",
                            channel = "testId1"
                        )
                    )
                ),
                SingleChannelWithPrograms(
                    date = (time + 25.hours).toEpochMilliseconds().convertDateToReadableFormat(),
                    programs = listOf(
                        Program(
                            (time + 25.hours).toEpochMilliseconds(),
                            (time + 26.hours).toEpochMilliseconds(),
                            "title 4",
                            channel = "testId1"
                        ),
                        Program(
                            (time + 26.hours).toEpochMilliseconds(),
                            (time + 27.hours).toEpochMilliseconds(),
                            "title 5",
                            channel = "testId1"
                        ),
                    )
                ),
            )

            coEvery {
                channelProgramRepository.loadProgramsForChannel("testId1")
            } returns expectedPrograms

            withClue("call sequence from selectedChannelRepository execute") {
                runTest {
                    sortedProgramUseCase.retrieveProgramsForChannel("testId1")

                    coVerify(exactly = 1) {
                        channelProgramRepository.loadProgramsForChannel("testId1")
                    }
                }
            }

            withClue("result from selectedChannelRepository validate") {
                val retrievedResult = sortedProgramUseCase.retrieveProgramsForChannel("testId1")

                withClue("result is list of string value") {
                    retrievedResult.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
                    retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
                }

                withClue("result elements proper count") {
                    retrievedResult.count() shouldBe expectedResult.count()
                    retrievedResult.first().programs.count() shouldBe expectedResult.first().programs.count()
                    retrievedResult.last().programs.count() shouldBe expectedResult.last().programs.count()
                }

                withClue("result fields values match expected values first items") {
                    retrievedResult.first().date shouldBe expectedResult.first().date
                    retrievedResult.first().programs.first().title shouldBe expectedResult.first().programs.first().title
                }

                withClue("result fields values match expected values last items") {
                    retrievedResult.last().date shouldBe expectedResult.last().date
                    retrievedResult.last().programs.first().title shouldBe "title 4"
                    retrievedResult.last().programs.last().title shouldBe "title 5"
                }
            }
        }

        "check programs update required" {
            withClue("programs update is required") {
                coEvery {
                    channelProgramRepository.loadProgramsCount(expectedResultDao.map { it.channelId })
                } returns 4

                val obtainedIds = expectedResultDao.take(4).map { it.channelId }
                val retrievedResult = sortedProgramUseCase.checkProgramsUpdateRequired(obtainedIds)

                retrievedResult shouldNotBe null
                retrievedResult?.count() shouldBe 2
                retrievedResult?.last() shouldBe "testId6"
            }

            withClue("programs update is noy required") {
                coEvery {
                    channelProgramRepository.loadProgramsCount(expectedResultDao.map { it.channelId })
                } returns 6

                val obtainedIds = expectedResultDao.map { it.channelId }
                val retrievedResult = sortedProgramUseCase.checkProgramsUpdateRequired(obtainedIds)

                retrievedResult shouldBe null
            }
        }

        "get sorted programs for selected channels" {
            val time = Clock.System.now()

            val expectedPrograms = listOf(
                Program(
                    time.toEpochMilliseconds(),
                    (time + 1.hours).toEpochMilliseconds(),
                    "title 1",
                    channel = "testId1"
                ),
                Program(
                    (time + 1.hours).toEpochMilliseconds(),
                    (time + 2.hours).toEpochMilliseconds(),
                    "title 2",
                    channel = "testId1"
                ),
                Program(
                    (time + 2.hours).toEpochMilliseconds(),
                    (time + 3.hours).toEpochMilliseconds(),
                    "title 3",
                    channel = "testId2"
                ),
                Program(
                    (time + 3.hours).toEpochMilliseconds(),
                    (time + 4.hours).toEpochMilliseconds(),
                    "title 4",
                    channel = "testId2"
                ),
                Program(
                    (time + 25.hours).toEpochMilliseconds(),
                    (time + 26.hours).toEpochMilliseconds(),
                    "title 5",
                    channel = "testId1"
                ),
                Program(
                    (time + 25.hours).toEpochMilliseconds(),
                    (time + 26.hours).toEpochMilliseconds(),
                    "title 6",
                    channel = "testId2"
                ),
                Program(
                    (time + 26.hours).toEpochMilliseconds(),
                    (time + 27.hours).toEpochMilliseconds(),
                    "title 7",
                    channel = "testId1"
                )
            )

            val expectedResult = listOf(
                SelectedChannelWithPrograms(
                    selectedChannel = SelectedChannel(
                        "testId1",
                        "testName1",
                        "iconUrl",
                        order = 1,
                        parentList = "test"
                    ),
                    programs = listOf(
                        Program(
                            time.toEpochMilliseconds(),
                            (time + 1.hours).toEpochMilliseconds(),
                            "title 1",
                            channel = "testId1"
                        ),
                        Program(
                            (time + 1.hours).toEpochMilliseconds(),
                            (time + 2.hours).toEpochMilliseconds(),
                            "title 2",
                            channel = "testId1"
                        ),
                        Program(
                            (time + 25.hours).toEpochMilliseconds(),
                            (time + 26.hours).toEpochMilliseconds(),
                            "title 5",
                            channel = "testId1"
                        )
                    )
                ),
                SelectedChannelWithPrograms(
                    selectedChannel = SelectedChannel(
                        "testId2",
                        "testName2",
                        "iconUrl",
                        order = 2,
                        parentList = "test"
                    ),
                    programs = listOf(
                        Program(
                            (time + 2.hours).toEpochMilliseconds(),
                            (time + 3.hours).toEpochMilliseconds(),
                            "title 3",
                            channel = "testId2"
                        ),
                        Program(
                            (time + 3.hours).toEpochMilliseconds(),
                            (time + 4.hours).toEpochMilliseconds(),
                            "title 4",
                            channel = "testId2"
                        ),
                        Program(
                            (time + 25.hours).toEpochMilliseconds(),
                            (time + 26.hours).toEpochMilliseconds(),
                            "title 6",
                            channel = "testId2"
                        )
                    )
                )
            )

            coEvery {
                preferenceRepository.loadAppSettings()
            } returns flow {
                emit(AppSettingsModel())
            }

            coEvery {
                channelProgramRepository.loadProgramsForChannels(any())
            } returns expectedPrograms

            val retrievedResult = sortedProgramUseCase.retrieveSelectedChannelWithPrograms()

            withClue("result is list of selected channels and programs value") {
                retrievedResult.shouldBeInstanceOf<List<SelectedChannelWithPrograms>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResult.count()
            }

            withClue("result elements proper default view count from settings") {
                retrievedResult.first().programs.count() shouldBe 3
                retrievedResult.last().programs.count() shouldBe expectedResult.last().programs.count()
            }

            withClue("result fields values match expected values first items") {
                retrievedResult.first().selectedChannel.channelName shouldBe expectedResult.first().selectedChannel.channelName
                retrievedResult.first().programs.first().title shouldBe expectedResult.first().programs.first().title
            }

            withClue("result fields values match expected values last items") {
                retrievedResult.last().selectedChannel.channelName shouldBe expectedResult.last().selectedChannel.channelName
                retrievedResult.last().programs.first().title shouldBe "title 3"
                retrievedResult.last().programs.last().title shouldBe "title 6"
            }
        }

        "update program schedule" {
            val programSchedule = ProgramSchedule(
                channelId = "channelId",
                programTitle = "testTitle",
            )

            coEvery {
                channelProgramRepository.loadProgramsForChannel(any())
            } returns listOf(
                Program(
                    Clock.System.now().toEpochMilliseconds(),
                    (Clock.System.now() + 2.hours).toEpochMilliseconds(),
                    "testTitle",
                )
            )

            sortedProgramUseCase.updateProgramScheduleWithAlarm(programSchedule)

            coVerifySequence {
                channelProgramRepository.loadProgramsForChannel(programSchedule.channelId)
                selectedChannelRepository.loadChannelNameById(programSchedule.channelId)
                programSchedulerHelper.scheduleProgramAlarm(any())
                channelProgramRepository.updateProgram(any())
            }
        }

        "cancel program schedule" {
            val programSchedule = ProgramSchedule(
                channelId = "channelId",
                programTitle = "testTitle",
            )

            coEvery {
                channelProgramRepository.loadProgramsForChannel(any())
            } returns listOf(
                Program(
                    Clock.System.now().toEpochMilliseconds(),
                    (Clock.System.now() + 2.hours).toEpochMilliseconds(),
                    "testTitle",
                    scheduledId = 1111L
                )
            )

            sortedProgramUseCase.updateProgramScheduleWithAlarm(programSchedule)

            coVerifySequence {
                channelProgramRepository.loadProgramsForChannel(programSchedule.channelId)
                selectedChannelRepository.loadChannelNameById(programSchedule.channelId)
                programSchedulerHelper.cancelProgramAlarm(any())
                channelProgramRepository.updateProgram(any())
            }
        }

        "no match channel" {
            val programSchedule = ProgramSchedule(
                channelId = "channelId",
                programTitle = "testTitle",
            )

            coEvery {
                channelProgramRepository.loadProgramsForChannel(any())
            } returns listOf(
                Program(
                    Clock.System.now().toEpochMilliseconds(),
                    (Clock.System.now() + 2.hours).toEpochMilliseconds(),
                    "anotherTitle",
                    scheduledId = 1111L
                )
            )

            sortedProgramUseCase.updateProgramScheduleWithAlarm(programSchedule)

            coVerifySequence {
                channelProgramRepository.loadProgramsForChannel(programSchedule.channelId)
            }
        }
    }
})

private fun createChannelProgramMockRepository(): ChannelProgramRepository {
    val channelProgramRepository = mockk<ChannelProgramRepository>()

    coEvery {
        channelProgramRepository.updateProgram(any())
    } just runs

    return channelProgramRepository
}

private fun createPreferenceMockRepository(): PreferenceRepository {
    val preferenceRepository = mockk<PreferenceRepository>()

    coEvery {
        preferenceRepository.loadDefaultUserList()
    } returns flow {
        emit("test")
    }
    return preferenceRepository
}

private fun createProgramSchedulerHelper(): ProgramSchedulerHelper {
    val programSchedulerHelper = mockk<ProgramSchedulerHelper>()

    coEvery {
        programSchedulerHelper.scheduleProgramAlarm(any())
    } just runs

    coEvery {
        programSchedulerHelper.cancelProgramAlarm(any())
    } just runs

    return programSchedulerHelper
}

private fun createSelectedChannelMockRepository(): SelectedChannelRepository {
    val selectedChannelRepository = mockk<SelectedChannelRepository>()

    coEvery {
        selectedChannelRepository.loadSelectedChannelsFlow("test")
    } returns flow {
        emit(expectedResultDao)
    }

    coEvery {
        selectedChannelRepository.loadSelectedChannels("test")
    } returns expectedResultWithIconDao

    coEvery {
        selectedChannelRepository.loadChannelNameById(any())
    } returns "channelName"

    return selectedChannelRepository
}

private val expectedResultDao
    get() = listOf(
        SelectedChannelEntity("testId1", "testName1", order = 1, parentList = "test"),
        SelectedChannelEntity("testId2", "testName2", order = 2, parentList = "test"),
        SelectedChannelEntity("testId3", "testName3", order = 3, parentList = "test"),
        SelectedChannelEntity("testId4", "testName4", order = 4, parentList = "test"),
        SelectedChannelEntity("testId5", "testName5", order = 5, parentList = "test"),
        SelectedChannelEntity("testId6", "testName6", order = 6, parentList = "test"),
    )
private val expectedResultWithIconDao
    get() = listOf(
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId1", "testName1", order = 1, parentList = "test"),
            allChannel = AvailableChannelEntity("testId1", "testName1", "iconUrl")
        ),
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId2", "testName2", order = 2, parentList = "test"),
            allChannel = AvailableChannelEntity("testId2", "testName2", "iconUrl")
        ),
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId3", "testName3", order = 3, parentList = "test"),
            allChannel = AvailableChannelEntity("testId3", "testName3", "iconUrl")
        ),
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId4", "testName4", order = 4, parentList = "test"),
            allChannel = AvailableChannelEntity("testId4", "testName4", "iconUrl")
        ),
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId5", "testName5", order = 5, parentList = "test"),
            allChannel = AvailableChannelEntity("testId5", "testName5", "iconUrl")
        ),
        SelectedChannelWithIconEntity(
            channel = SelectedChannelEntity("testId6", "testName6", order = 6, parentList = "test"),
            allChannel = AvailableChannelEntity("testId6", "testName6", "iconUrl")
        )
    )
