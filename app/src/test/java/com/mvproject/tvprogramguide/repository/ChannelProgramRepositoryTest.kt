package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class ChannelProgramRepositoryTest : StringSpec({

    assertSoftly {
        "scheduled load program verify calls" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<ProgramDao>(relaxed = true)
            val repository = ChannelProgramRepository(epg, dao)

            repository.loadProgram("test")
            coVerify(exactly = 1) {
                epg.getChannelProgram("test")
                dao.deletePrograms("test")
                dao.insertPrograms(listOf())
            }
        }

        "update program verify calls" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<ProgramDao>(relaxed = true)
            val repository = ChannelProgramRepository(epg, dao)

            val program = Program(
                1661764500000L,
                1661765400000L,
                "program"
            )

            val programEntity = program.toEntity()

            repository.updateProgram(program)

            coVerify(exactly = 1) {
                dao.updateProgram(programEntity)
            }
        }

        "loadProgramsCount returns proper data" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<ProgramDao>(relaxed = true)
            val repository = ChannelProgramRepository(epg, dao)

            val programEntity = listOf(
                ProgramEntity(1661764500000L, 1661765400000L, "entity1", channelId = "id1"),
                ProgramEntity(1661765400000L, 1661766500000L, "entity2", channelId = "id2"),
                ProgramEntity(1661766500000L, 1661767600000L, "entity3", channelId = "id1")
            )

            coEvery {
                dao.getSelectedChannelPrograms(any())
            } returns programEntity

            val result = repository.loadProgramsCount(listOf())

            result shouldBe 2
            result.shouldBeInstanceOf<Int>()
        }

        "loadProgramsForChannel for single id returns proper data" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<ProgramDao>(relaxed = true)
            val repository = ChannelProgramRepository(epg, dao)

            val testEntityList = programEntitySingleId

            coEvery {
                dao.getSingleChannelPrograms(any())
            } returns testEntityList

            val result = repository.loadProgramsForChannel("id1")

            result.count() shouldBe 3
            result.shouldBeInstanceOf<List<Program>>()
            result.first() shouldBeEqualToComparingFields testEntityList.first().toProgram()
            result.first().channel shouldBe "id1"
            result.first().title shouldBe "entity1"
        }

        "loadProgramsForChannel for many ids returns proper data3" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<ProgramDao>(relaxed = true)
            val repository = ChannelProgramRepository(epg, dao)

            val testEntityList = programEntityManyId

            coEvery {
                dao.getSelectedChannelPrograms(any())
            } returns testEntityList

            val result = repository.loadProgramsForChannels(listOf("id1"))

            result.count() shouldBe 3
            result.shouldBeInstanceOf<List<Program>>()
            result.first() shouldBeEqualToComparingFields testEntityList.first().toProgram()
            result.first().channel shouldBe "id1"
            result.first().title shouldBe "entity1"
            result.last().channel shouldBe "id3"
            result.last().title shouldBe "entity3"
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