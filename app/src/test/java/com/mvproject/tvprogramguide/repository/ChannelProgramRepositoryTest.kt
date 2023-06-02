package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class ChannelProgramRepositoryTest : StringSpec({
    lateinit var epg: EpgService
    lateinit var dao: ProgramDao
    lateinit var repository: ChannelProgramRepository

    beforeTest {
        epg = mockk<EpgService>(relaxed = true)
        dao = mockk<ProgramDao>(relaxed = true)
        repository = ChannelProgramRepository(epg, dao)
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    assertSoftly {
        "scheduled load program verify calls" {
            withClue("proper calls from dao execute") {
                repository.loadProgram("test")

                coVerifySequence {
                    epg.getChannelProgram("test")
                    dao.deletePrograms("test")
                    dao.insertPrograms(listOf())
                }
            }
        }

        "update program verify calls" {
            val program = Program(
                1661764500000L,
                1661765400000L,
                "program"
            )

            val programEntity = program.toEntity()

            withClue("single call from epg execute") {
                repository.updateProgram(program)

                coVerify(exactly = 1) {
                    dao.updateProgram(programEntity)
                }
                confirmVerified(dao)
            }
        }

        "loadProgramsCount returns proper data" {
            val expectedResultDao = listOf(
                ProgramEntity(1661764500000L, 1661765400000L, "entity1", channelId = "id1"),
                ProgramEntity(1661765400000L, 1661766500000L, "entity2", channelId = "id2"),
                ProgramEntity(1661766500000L, 1661767600000L, "entity3", channelId = "id1")
            )

            coEvery {
                dao.getSelectedChannelPrograms(any())
            } returns expectedResultDao

            val retrievedResult = repository.loadProgramsCount(listOf())

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSelectedChannelPrograms(any())
                }
                confirmVerified(dao)
            }

            withClue("result is list of entity value") {
                retrievedResult.shouldBeInstanceOf<Int>()
            }

            withClue("result elements proper count") {
                retrievedResult shouldBe 2
            }
        }

        "loadProgramsForChannel for single id returns proper data" {
            val expectedResultDao = programEntitySingleId

            coEvery {
                dao.getSingleChannelPrograms(any())
            } returns expectedResultDao

            val retrievedResult = repository.loadProgramsForChannel("id1")

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSingleChannelPrograms(any())
                }
                confirmVerified(dao)
            }

            withClue("result is list of entity value") {
                retrievedResult.shouldBeInstanceOf<List<Program>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResultDao.first()
                    .toProgram()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe 3
            }

            withClue("result fields values match expected values first item") {
                retrievedResult.first().channel shouldBe "id1"
                retrievedResult.first().title shouldBe "entity1"
            }

            withClue("result fields values match expected values last item") {
                retrievedResult.last().channel shouldBe "id1"
                retrievedResult.last().title shouldBe "entity3"
            }
        }

        "loadProgramsForChannel for many ids returns proper data" {
            val expectedResult = programEntityManyId

            coEvery {
                dao.getSelectedChannelPrograms(any())
            } returns expectedResult

            val retrievedResult = repository.loadProgramsForChannels(listOf("id1"))

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSelectedChannelPrograms(any())
                }
                confirmVerified(dao)
            }

            withClue("result is list of entity value") {
                retrievedResult.shouldBeInstanceOf<List<Program>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
                    .toProgram()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResult.count()
            }

            withClue("result fields values match expected values first item") {
                retrievedResult.first().channel shouldBe expectedResult.first().channelId
                retrievedResult.first().title shouldBe expectedResult.first().title
            }

            withClue("result fields values match expected values last item") {
                retrievedResult.last().channel shouldBe "id3"
                retrievedResult.last().title shouldBe "entity3"
            }
        }
    }
})

private val programEntitySingleId
    get() = listOf(
        ProgramEntity(
            (Clock.System.now() + 1.hours).toEpochMilliseconds(),
            (Clock.System.now() + 2.hours).toEpochMilliseconds(),
            "entity1",
            channelId = "id1"
        ),
        ProgramEntity(
            (Clock.System.now() + 2.hours).toEpochMilliseconds(),
            (Clock.System.now() + 3.hours).toEpochMilliseconds(),
            "entity2",
            channelId = "id1"
        ),
        ProgramEntity(
            (Clock.System.now() + 3.hours).toEpochMilliseconds(),
            (Clock.System.now() + 4.hours).toEpochMilliseconds(),
            "entity3",
            channelId = "id1"
        )
    )

private val programEntityManyId
    get() = listOf(
        ProgramEntity(
            (Clock.System.now() + 1.hours).toEpochMilliseconds(),
            (Clock.System.now() + 2.hours).toEpochMilliseconds(),
            "entity1",
            channelId = "id1"
        ),
        ProgramEntity(
            (Clock.System.now() + 2.hours).toEpochMilliseconds(),
            (Clock.System.now() + 3.hours).toEpochMilliseconds(),
            "entity2",
            channelId = "id2"
        ),
        ProgramEntity(
            (Clock.System.now() + 3.hours).toEpochMilliseconds(),
            (Clock.System.now() + 4.hours).toEpochMilliseconds(),
            "entity3",
            channelId = "id3"
        )
    )
