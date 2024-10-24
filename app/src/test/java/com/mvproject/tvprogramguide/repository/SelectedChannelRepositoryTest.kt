package com.mvproject.tvprogramguide.repository


import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
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
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class SelectedChannelRepositoryTest : FunSpec({

    lateinit var selectedChannelDao: SelectedChannelDao
    lateinit var repository: SelectedChannelRepository

    beforeTest {
        selectedChannelDao = mockk()
        repository = SelectedChannelRepository(selectedChannelDao)
    }

    afterTest {
        unmockkAll()
    }

    context("loadSelectedChannels") {
        test("should return mapped SelectionChannel list when channels exist") {
            val listName = "TestList"

            val selectedChannels = listOf(
                SelectedChannelWithIconEntity(
                    SelectedChannelEntity("1", "programId1" , "Channel 1"),
                    AvailableChannelEntity("1", "programId1", "Channel 1", "logo1")
                ),
                SelectedChannelWithIconEntity(
                    SelectedChannelEntity("2" ,"programId2", "Channel 2",),
                    AvailableChannelEntity("2", "programId2", "Channel 2", "logo2")
                )
            )

            coEvery { selectedChannelDao.getSelectedChannels(listName) } returns selectedChannels

            val result = repository.loadSelectedChannels(listName)

            withClue("Returned list should match expected SelectionChannel list") {
                result shouldBe listOf(
                    SelectionChannel("1", "programId1", "Channel 1", "logo1"),
                    SelectionChannel("2", "programId2", "Channel 2", "logo2")
                )
            }

            coVerify { selectedChannelDao.getSelectedChannels(listName) }
        }

        test("should return empty list when no channels exist")  {
            val listName = "EmptyList"
            coEvery { selectedChannelDao.getSelectedChannels(listName) } returns emptyList()

            val result = repository.loadSelectedChannels(listName)

            withClue("Returned list should be empty") {
                result.shouldBe(emptyList())
            }

            coVerify { selectedChannelDao.getSelectedChannels(listName) }
        }
    }

    context("loadSelectedChannelsAsFlow") {
        test("should return mapped SelectionChannel list as Flow when channels exist")  {
            val selectedChannels = listOf(
                SelectedChannelWithIconEntity(
                    SelectedChannelEntity("1", "programId1", "Channel 1"),
                    AvailableChannelEntity("1", "programId1", "Channel 1", "logo1")
                ),
                SelectedChannelWithIconEntity(
                    SelectedChannelEntity("2", "programId2", "Channel 2"),
                    AvailableChannelEntity("2", "programId2", "Channel 2", "logo2")
                )
            )

            every { selectedChannelDao.getChannelsForCurrentListAsFlow() } returns flowOf(selectedChannels)

            val result = repository.loadSelectedChannelsAsFlow().first()

            withClue("Returned Flow should emit expected SelectionChannel list") {
                result shouldBe listOf(
                    SelectionChannel("1", "programId1", "Channel 1", "logo1"),
                    SelectionChannel("2", "programId2", "Channel 2", "logo2")
                )
            }

            verify { selectedChannelDao.getChannelsForCurrentListAsFlow() }
        }

        test("should return empty list as Flow when no channels exist") {
            every { selectedChannelDao.getChannelsForCurrentListAsFlow() } returns flowOf(emptyList())

            val result = repository.loadSelectedChannelsAsFlow().first()

            withClue("Returned Flow should emit an empty list") {
                result.shouldBe(emptyList())
            }

            verify { selectedChannelDao.getChannelsForCurrentListAsFlow() }
        }
    }

    context("addChannels") {
        test("should add channels correctly") {
            val listName = "NewList"
            val channelsToAdd = listOf(
                SelectedChannelEntity("1", "Channel 1", "programId1", 1, listName),
                SelectedChannelEntity("2", "Channel 2", "programId2", 2, listName)
            )

            coEvery { selectedChannelDao.deleteChannels(listName) } just Runs
            coEvery { selectedChannelDao.insertChannels(channelsToAdd) } just Runs

            repository.addChannels(listName, channelsToAdd)

            coVerifyOrder {
                selectedChannelDao.deleteChannels(listName)
                selectedChannelDao.insertChannels(channelsToAdd)
            }
        }

        test("should handle empty list of channels")  {
            val listName = "EmptyList"

            coEvery { selectedChannelDao.deleteChannels(listName) } just Runs
            coEvery { selectedChannelDao.insertChannels(emptyList()) } just Runs

            repository.addChannels(listName, emptyList())

            coVerifyOrder {
                selectedChannelDao.deleteChannels(listName)
                selectedChannelDao.insertChannels(emptyList())
            }
        }

        test("should handle transaction failure")  {
            val listName = "FailList"
            val channelsToAdd = listOf(
                SelectedChannelEntity("1", "Channel 1", "programId1", 1, listName)
            )

            coEvery { selectedChannelDao.deleteChannels(listName) } throws RuntimeException("Database error")

            kotlin.runCatching {
                repository.addChannels(listName, channelsToAdd)
            }

            coVerify(exactly = 1) {
                selectedChannelDao.deleteChannels(listName)
            }
            coVerify(exactly = 0) {
                selectedChannelDao.insertChannels(any())
            }
        }
    }
})
