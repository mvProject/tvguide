package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

class SelectedChannelRepositoryTest : StringSpec({
    lateinit var dao: SelectedChannelDao
    lateinit var repository: SelectedChannelRepository

    beforeTest {
        dao = mockk<SelectedChannelDao>(relaxed = true)
        repository = SelectedChannelRepository(dao)
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    assertSoftly {
        "retrieve selected channels entities" {
            val expectedResultDao =
                listOf(
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId1",
                                "testName1",
                                order = 1,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId1", "testName1", "iconUrl1"),
                    ),
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId2",
                                "testName2",
                                order = 2,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId2", "testName2", "iconUrl2"),
                    ),
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId3",
                                "testName3",
                                order = 3,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId3", "testName3", "iconUrl3"),
                    ),
                )
            coEvery {
                dao.getSelectedChannels("test")
            } returns expectedResultDao

            val retrievedResult = repository.loadSelectedChannels("test")

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getSelectedChannels("test")
                }
                confirmVerified(dao)
            }

            withClue("result is list of entity value") {
                retrievedResult.shouldBeInstanceOf<List<SelectedChannelWithIconEntity>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResultDao.first()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResultDao.count()
            }

            withClue("result fields values match expected values first item") {
                retrievedResult.first().channel.channelId shouldBe expectedResultDao.first().channel.channelId
                retrievedResult.first().channel.channelName shouldBe expectedResultDao.first().channel.channelName
            }

            withClue("result fields values match expected values last item") {
                retrievedResult.last().channel.channelId shouldBe "testId3"
                retrievedResult.last().channel.channelName shouldBe "testName3"
            }
        }

        "retrieve selected channels ids" {
            val expectedResult =
                listOf(
                    "testId1",
                    "testId2",
                    "testId3",
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
            val testChannel = SelectedChannelEntity("testId", "testName", 1, "test")

            withClue("single call from dao execute") {
                repository.addChannel(testChannel)

                coVerify(exactly = 1) {
                    dao.insertChannel(testChannel)
                }
                confirmVerified(dao)
            }
        }

        "delete selected channel" {
            val testChannel = SelectedChannelEntity("testId", "testName", 1, "test")

            withClue("single call from dao execute") {
                repository.deleteChannel(testChannel.channelId)

                coVerify(exactly = 1) {
                    dao.deleteSelectedChannel(testChannel.channelId)
                }
                confirmVerified(dao)
            }
        }

        "update selected channels" {
            val testChannel = SelectedChannelEntity("testId", "testName", 1, "test")

            withClue("single call from dao execute") {
                repository.updateChannels(listOf(testChannel))

                coVerify(exactly = 1) {
                    dao.updateChannels(listOf(testChannel))
                }
                confirmVerified(dao)
            }
        }

        "retrieve selected channels entities flow" {
            val expectedResultDao =
                listOf(
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId1",
                                "testName1",
                                order = 1,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId1", "testName1", "iconUrl1"),
                    ),
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId2",
                                "testName2",
                                order = 2,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId2", "testName2", "iconUrl2"),
                    ),
                    SelectedChannelWithIconEntity(
                        channel =
                            SelectedChannelEntity(
                                "testId3",
                                "testName3",
                                order = 3,
                                parentList = "test",
                            ),
                        allChannel = AvailableChannelEntity("testId3", "testName3", "iconUrl3"),
                    ),
                )

            coEvery {
                dao.getSelectedChannelsFlow("test")
            } returns
                flow {
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
                            list.shouldBeInstanceOf<List<SelectedChannelWithIconEntity>>()
                            list.first() shouldBeEqualToComparingFields expectedResultDao.first()
                        }

                        withClue("result elements proper count") {
                            list.count() shouldBe 3
                        }

                        withClue("result fields values match expected values first item") {
                            list.first().channel.channelId shouldBe "testId1"
                            list.first().channel.channelName shouldBe "testName1"
                        }

                        withClue("result fields values match expected values last item") {
                            list.last().channel.channelId shouldBe "testId3"
                            list.last().channel.channelName shouldBe "testName3"
                        }
                    }
                }
            }
        }
    }
})
