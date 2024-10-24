package com.mvproject.tvprogramguide.repository


import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll

class ProgramRepositoryTest : FunSpec({

    lateinit var programDao: ProgramDao
    lateinit var repository: ProgramRepository

    beforeTest {
        programDao = mockk()
        repository = ProgramRepository(programDao)
    }

    afterTest {
        unmockkAll()
    }

    context("loadProgramsForChannels") {
        test("should return mapped Program list when programs exist")  {
            val channelIds = listOf("1", "2")
            val programEntities = listOf(
                ProgramEntity("1", 1000L, 2000L, "Program 1", "Description 1", "1"),
                ProgramEntity("2", 2000L, 3000L, "Program 2", "Description 2", "2")
            )

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns 500L
            coEvery { programDao.getSelectedChannelPrograms(500L, channelIds) } returns programEntities

            val result = repository.loadProgramsForChannels(channelIds)

            withClue("Returned list should match expected Program list") {
                result shouldBe listOf(
                    Program("1", 1000L, 2000L, "Program 1", "Description 1", "1"),
                    Program("2", 2000L, 3000L, "Program 2", "Description 2", "2")
                )
            }

            coVerify { programDao.getSelectedChannelPrograms(500L, channelIds) }
        }

        test("should return empty list when no programs exist")  {
            val channelIds = listOf("1", "2")

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns 500L
            coEvery { programDao.getSelectedChannelPrograms(500L, channelIds) } returns emptyList()

            val result = repository.loadProgramsForChannels(channelIds)

            withClue("Returned list should be empty") {
                result.shouldBe(emptyList())
            }

            coVerify { programDao.getSelectedChannelPrograms(500L, channelIds) }
        }
    }

    context("loadProgramsForChannel") {
        test("should return mapped Program list for a single channel") {
            val channelId = "1"
            val programEntities = listOf(
                ProgramEntity("1", 1000L, 2000L,"Program 1", "Description 1",  channelId),
                ProgramEntity("2", 2000L, 3000L,"Program 2", "Description 2",  channelId)
            )

            mockkObject(TimeUtils)
            every { TimeUtils.actualDate } returns 500L
            coEvery { programDao.getChannelProgramsById(500L, channelId) } returns programEntities

            val result = repository.loadProgramsForChannel(channelId)

            withClue("Returned list should match expected Program list for the channel") {
                result shouldBe listOf(
                    Program("1", 1000L, 2000L,"Program 1", "Description 1",  channelId),
                    Program("2", 2000L, 3000L,"Program 2", "Description 2",  channelId)
                )
            }

            coVerify { programDao.getChannelProgramsById(500L, channelId) }
        }
    }

    context("updatePrograms") {
        test("should update programs correctly") {
            val channelId = "1"
            val programDTOs = listOf(
                ProgramDTO(202107011200, 202107011300,"Program 1", "Description 1"),
                ProgramDTO(202107011300, 202107011400,"Program 2", "Description 2")
            )

            coEvery { programDao.deletePrograms(channelId) } just Runs
            coEvery { programDao.insertPrograms(any()) } just Runs

            repository.updatePrograms(channelId, programDTOs)

            coVerifyOrder {
                programDao.deletePrograms(channelId)
                programDao.insertPrograms(any())
            }
        }
    }

    context("cleanProgramsBeforeDate") {
        test("should delete programs before given date") {
            val date = 1000L
            coEvery { programDao.deleteProgramsByDate(date) } just Runs

            repository.cleanProgramsBeforeDate(date)

            coVerify { programDao.deleteProgramsByDate(date) }
        }
    }

    context("updateProgram") {
        test("should update a single program") {
            val program = Program("1", 1000L, 2000L,"Updated Program", "New Description",  "1")
            coEvery { programDao.updateProgram(any()) } just Runs

            repository.updateProgram(program)

            coVerify { programDao.updateProgram(any()) }
        }
    }
})