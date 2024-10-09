package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CustomListRepositoryTest :
    StringSpec({
        lateinit var dao: ChannelsListDao
        lateinit var repository: ChannelListRepository

        beforeTest {
            dao = mockk<ChannelsListDao>(relaxed = true)
            repository = ChannelListRepository(dao)
        }

        afterTest {
            println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
        }

        assertSoftly {

            "delete list from database" {
                coEvery {
                    dao.deleteSingleChannelsList(1)
                } just runs

                withClue("single call from dao execute") {
                    repository.deleteList(ChannelList(1, "1", false))

                    coVerify(exactly = 1) {
                        dao.deleteSingleChannelsList(1)
                    }
                    confirmVerified(dao)
                }
            }

            "add list to database if name filled" {
                val repository = mockk<ChannelListRepository>()

                coEvery {
                    dao.addChannelsList(any())
                } just runs

                coEvery {
                    repository.addChannelsList("list")
                } just runs

                withClue("single call from repository execute") {
                    repository.addChannelsList("list")

                    coVerify(exactly = 1) {
                        repository.addChannelsList("list")
                    }
                    confirmVerified(repository)
                }
            }

            "add list to database if name empty" {
                coEvery {
                    dao.addChannelsList(ChannelsListEntity(1, "", false))
                } just runs

                withClue("single call from dao execute") {
                    repository.addChannelsList("")

                    coVerify(exactly = 0) {
                        dao.addChannelsList(ChannelsListEntity(1, "", false))
                    }
                    confirmVerified(dao)
                }
            }

            "retrieve userlists" {
                val expectedResultDao =
                    listOf(
                        ChannelsListEntity(1, "list1", false),
                        ChannelsListEntity(2, "list2", false),
                        ChannelsListEntity(3, "list3", false),
                    )

                val expectedResult =
                    listOf(
                        ChannelList(1, "list1", false),
                        ChannelList(2, "list2", false),
                        ChannelList(3, "list3", false),
                    )

                coEvery {
                    dao.getChannelsListsAsFlow()
                } returns
                    flow {
                        emit(expectedResultDao)
                    }

                withClue("single call from dao execute") {
                    repository.loadChannelsLists()

                    coVerify(exactly = 1) {
                        dao.getChannelsLists()
                    }
                    confirmVerified(dao)
                }

                withClue("collect proper data from flow") {
                    runTest {
                        repository.loadChannelsListsAsFlow().collect { retrievedResult ->
                            withClue("result is list of entity value") {
                                retrievedResult.shouldBeInstanceOf<List<ChannelList>>()
                                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
                            }
                            withClue("result fields values match expected values") {
                                retrievedResult.last().listName shouldBe "list3"
                                retrievedResult.last().id shouldBe 3
                            }
                        }
                    }
                }
            }
        }
    })
