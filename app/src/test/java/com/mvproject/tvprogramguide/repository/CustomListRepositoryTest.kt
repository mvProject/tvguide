package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.UserChannelsListDao
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.model.entity.UserChannelsListEntity
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*
import kotlinx.coroutines.flow.flow

class CustomListRepositoryTest : StringSpec({
    assertSoftly {
        withClue("test delete list") {
            "delete list from database" {
                val dao = mockk<UserChannelsListDao>()
                val repository = CustomListRepository(dao)

                coEvery {
                    dao.deleteSingleUserChannelsList(1)
                } just runs

                runBlocking {
                    repository.deleteList(UserChannelsList(1, "1"))
                }

                coVerify(exactly = 1) {
                    dao.deleteSingleUserChannelsList(1)
                }
            }
        }

        withClue("test add list proper name") {
            "add list to database if name filled" {
                val repository = mockk<CustomListRepository>()

                coEvery {
                    repository.addCustomList("list")
                } just runs

                runBlocking {
                    repository.addCustomList("list")
                }
                coVerify(exactly = 1) {
                    repository.addCustomList("list")
                }
            }
        }

        withClue("test add list wrong name") {
            "add list to database if name empty" {
                val dao = mockk<UserChannelsListDao>(relaxed = true)
                val repository = CustomListRepository(dao)

                coEvery {
                    dao.addUserChannelsList(UserChannelsListEntity(1, ""))
                } just runs

                runBlocking {
                    repository.addCustomList("")
                }
                coVerify(exactly = 0) {
                    dao.addUserChannelsList(UserChannelsListEntity(1, ""))
                }
            }
        }

        withClue("get all lists") {
            "retrieve userlists" {
                val dao = mockk<UserChannelsListDao>()
                val repository = CustomListRepository(dao)

                val resultListEntity = listOf(
                    UserChannelsListEntity(1, "list1"),
                    UserChannelsListEntity(2, "list2"),
                    UserChannelsListEntity(3, "list3")
                )

                val resultList = listOf(
                    UserChannelsList(1, "list1"),
                    UserChannelsList(2, "list2"),
                    UserChannelsList(3, "list3")
                )

                coEvery {
                    dao.getAllUserChannelsLists()
                } returns flow {
                    emit(resultListEntity)
                }

                runBlocking {
                    repository.loadChannelsLists().collect { list ->
                        list.shouldBeInstanceOf<List<UserChannelsList>>()
                        list.first() shouldBeEqualToComparingFields resultList.first()
                        list.last().listName shouldBe "list3"
                        list.last().id shouldBe 3
                    }
                }
            }
        }
    }
})
