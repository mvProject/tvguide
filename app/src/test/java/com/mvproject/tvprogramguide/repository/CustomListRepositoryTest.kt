package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
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
        lateinit var repository: CustomListRepository

        beforeTest {
            dao = mockk<ChannelsListDao>(relaxed = true)
            repository = CustomListRepository(dao)
        }

        afterTest {
            println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
        }

        assertSoftly {

            "delete list from database" {
                coEvery {
                    dao.deleteSingleUserChannelsList(1)
                } just runs

                withClue("single call from dao execute") {
                    repository.deleteList(UserChannelsList(1, "1"))

                    coVerify(exactly = 1) {
                        dao.deleteSingleUserChannelsList(1)
                    }
                    confirmVerified(dao)
                }
            }

            "add list to database if name filled" {
                val repository = mockk<CustomListRepository>()

                coEvery {
                    dao.addUserChannelsList(any())
                } just runs

                coEvery {
                    repository.addCustomList("list")
                } just runs

                withClue("single call from repository execute") {
                    repository.addCustomList("list")

                    coVerify(exactly = 1) {
                        repository.addCustomList("list")
                    }
                    confirmVerified(repository)
                }
            }

            "add list to database if name empty" {
                coEvery {
                    dao.addUserChannelsList(ChannelsListEntity(1, ""))
                } just runs

                withClue("single call from dao execute") {
                    repository.addCustomList("")

                    coVerify(exactly = 0) {
                        dao.addUserChannelsList(ChannelsListEntity(1, ""))
                    }
                    confirmVerified(dao)
                }
            }

            "retrieve userlists" {
                val expectedResultDao =
                    listOf(
                        ChannelsListEntity(1, "list1"),
                        ChannelsListEntity(2, "list2"),
                        ChannelsListEntity(3, "list3"),
                    )

                val expectedResult =
                    listOf(
                        UserChannelsList(1, "list1"),
                        UserChannelsList(2, "list2"),
                        UserChannelsList(3, "list3"),
                    )

                coEvery {
                    dao.getAllUserChannelsLists()
                } returns
                    flow {
                        emit(expectedResultDao)
                    }

                withClue("single call from dao execute") {
                    repository.loadChannelsLists()

                    coVerify(exactly = 1) {
                        dao.getAllUserChannelsLists()
                    }
                    confirmVerified(dao)
                }

                withClue("collect proper data from flow") {
                    runTest {
                        repository.loadChannelsLists().collect { retrievedResult ->
                            withClue("result is list of entity value") {
                                retrievedResult.shouldBeInstanceOf<List<UserChannelsList>>()
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
