package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SelectedChannelRepositoryTest : StringSpec({
    assertSoftly {
        "retrieve selected channels entities" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val expectedResult = listOf(
                SelectedChannelEntity("testId1", "testName1", "iconUrl"),
                SelectedChannelEntity("testId2", "testName2", "iconUrl"),
                SelectedChannelEntity("testId3", "testName3", "iconUrl"),
            )
            coEvery {
                dao.getSelectedChannels("test")
            } returns expectedResult


            val retrievedResult = repository.loadSelectedChannels("test")

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSelectedChannels("test")
                }
                confirmVerified(dao)
            }

            withClue("result is list of entity value") {
                retrievedResult.shouldBeInstanceOf<List<SelectedChannelEntity>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResult.count()
            }

            withClue("result fields values match expected values first item") {
                retrievedResult.first().channelId shouldBe expectedResult.first().channelId
                retrievedResult.first().channelName shouldBe expectedResult.first().channelName
            }

            withClue("result fields values match expected values last item") {
                retrievedResult.last().channelId shouldBe "testId3"
                retrievedResult.last().channelName shouldBe "testName3"
            }
        }

        "retrieve selected channels ids" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val expectedResult = listOf(
                "testId1",
                "testId2",
                "testId3"
            )
            coEvery {
                dao.getSelectedChannelsIds()
            } returns expectedResult

            val retrievedResult = repository.loadSelectedChannelsIds()

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSelectedChannelsIds()
                }
                confirmVerified(dao)
            }

            withClue("result is list of string value") {
                retrievedResult.shouldBeInstanceOf<List<String>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResult.count()
            }

            withClue("result fields values match expected values") {
                retrievedResult.first() shouldBe expectedResult.first()
                retrievedResult.last() shouldBe "testId3"
            }
        }

        "retrieve selected channel name by id" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val expectedResult = "testName1"

            coEvery {
                dao.getChannelNameById("testId")
            } returns expectedResult


            val retrievedResult = repository.loadChannelNameById("testId")

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getChannelNameById("testId")
                }
                confirmVerified(dao)
            }

            withClue("result is string value") {
                retrievedResult.shouldBeInstanceOf<String>()
            }

            withClue("result value match expected value") {
                retrievedResult shouldBe "testName1"
            }
        }

        "add selected channel" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val testChannel = SelectedChannelEntity("testId", "testName", "iconUrl")

            withClue("single call from dao execute") {
                repository.addChannel(testChannel)

                coVerify(exactly = 1) {
                    dao.insertChannel(testChannel)
                }
                confirmVerified(dao)
            }
        }

        "delete selected channel" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val testChannel = SelectedChannelEntity("testId", "testName", "iconUrl")

            withClue("single call from dao execute") {
                repository.deleteChannel(testChannel.channelId)

                coVerify(exactly = 1) {
                    dao.deleteSelectedChannel(testChannel.channelId)
                }
                confirmVerified(dao)
            }
        }

        "update selected channels" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val testChannel = SelectedChannelEntity("testId", "testName", "iconUrl")

            withClue("single call from dao execute") {
                repository.updateChannels(listOf(testChannel))

                coVerify(exactly = 1) {
                    dao.updateChannels(listOf(testChannel))
                }
                confirmVerified(dao)
            }
        }

        "retrieve selected channels entities flow" {
            val dao = mockk<SelectedChannelDao>(relaxed = true)
            val repository = SelectedChannelRepository(dao)

            val expectedResultDao = listOf(
                SelectedChannelEntity("testId1", "testName1", "iconUrl"),
                SelectedChannelEntity("testId2", "testName2", "iconUrl"),
                SelectedChannelEntity("testId3", "testName3", "iconUrl"),
            )

            coEvery {
                dao.getSelectedChannelsFlow("test")
            } returns flow {
                emit(expectedResultDao)
            }

            withClue("single call from dao execute") {
                repository.loadSelectedChannelsFlow("test")

                coVerify(exactly = 1) {
                    dao.getSelectedChannelsFlow("test")
                }
                confirmVerified(dao)
            }

            withClue("collect proper data from flow") {
                runTest {
                    repository.loadSelectedChannelsFlow("test").collect { list ->
                        withClue("result is list of string value") {
                            list.shouldBeInstanceOf<List<SelectedChannelEntity>>()
                            list.first() shouldBeEqualToComparingFields expectedResultDao.first()
                        }

                        withClue("result elements proper count") {
                            list.count() shouldBe 3
                        }

                        withClue("result fields values match expected values first item") {
                            list.first().channelId shouldBe "testId1"
                            list.first().channelName shouldBe "testName1"
                        }

                        withClue("result fields values match expected values last item") {
                            list.last().channelId shouldBe "testId3"
                            list.last().channelName shouldBe "testName3"
                        }
                    }
                }
            }
        }
    }
})