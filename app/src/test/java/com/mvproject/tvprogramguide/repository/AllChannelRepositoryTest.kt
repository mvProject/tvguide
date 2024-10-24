package com.mvproject.tvprogramguide.repository


import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll

class AllChannelRepositoryTest : FunSpec({

    lateinit var allChannelDao: AllChannelDao
    lateinit var repository: AllChannelRepository

    beforeTest {
        allChannelDao = mockk()
        repository = AllChannelRepository(allChannelDao)
    }

    afterTest {
        unmockkAll()
    }

    context("loadChannels") {
        test("should return mapped SelectionChannel list when channels exist") {
            val availableChannels = listOf(
                AvailableChannelEntity("1", "programId1", "Channel 1", "logo1.png"),
                AvailableChannelEntity("2", "programId2", "Channel 2", "logo2.png")
            )

            coEvery { allChannelDao.getChannels() } returns availableChannels
            val result = repository.loadChannels()

            result shouldBe listOf(
                SelectionChannel("1", "programId1", "Channel 1", "logo1.png"),
                SelectionChannel("2", "programId2", "Channel 2", "logo2.png")
            )

            coVerify { allChannelDao.getChannels() }
        }

        test("should return empty list when no channels exist") {
            coEvery { allChannelDao.getChannels() } returns emptyList()

            val result = repository.loadChannels()
            result.shouldBe(emptyList())

            coVerify { allChannelDao.getChannels() }
        }
    }

    context("updateChannels") {
        test("should update channels correctly") {
            val channelResponses = listOf(
                AvailableChannelResponse("1", "Channel 1", "logo1.png"),
                AvailableChannelResponse("2", "Channel 2", "logo2.png")
            )
            val channelEntities = listOf(
                AvailableChannelEntity("1Channel1", "1","Channel 1", "logo1.png"),
                AvailableChannelEntity("2Channel2", "2", "Channel 2", "logo2.png")
            )

            coEvery { allChannelDao.deleteChannels() } just Runs
            coEvery { allChannelDao.insertChannels(any()) } just Runs

            repository.updateChannels(channelResponses)

            coVerify(exactly = 1) {
                allChannelDao.deleteChannels()
                allChannelDao.insertChannels(channelEntities)
            }
        }

        test("should handle empty list of channels") {
            coEvery { allChannelDao.deleteChannels() } just Runs
            coEvery { allChannelDao.insertChannels(any()) } just Runs

            repository.updateChannels(emptyList())

            coVerify(exactly = 1) {
                allChannelDao.deleteChannels()
                allChannelDao.insertChannels(emptyList())
            }
        }

        test("should handle transaction failure") {
            val channelResponses = listOf(
                AvailableChannelResponse("1", "Channel 1", "logo1.png")
            )

            coEvery { allChannelDao.deleteChannels() } throws RuntimeException("Database error")

            kotlin.runCatching {
                repository.updateChannels(channelResponses)
            }

            coVerify(exactly = 1) {
                allChannelDao.deleteChannels()
            }
            coVerify(exactly = 0) {
                allChannelDao.insertChannels(any())
            }
        }
    }
})