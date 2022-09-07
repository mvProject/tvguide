package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.response.AllAvailableChannelsResponse
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*

class AllChannelsRepositoryTest : StringSpec({

    assertSoftly {
        "loadProgramFromSource verify calls" {
            val epg = mockk<EpgService>()
            val dao = mockk<AllChannelDao>()
            val repository = AllChannelRepository(epg, dao)

            coEvery {
                epg.getChannels()
            } returns AllAvailableChannelsResponse(listOf())

            coEvery {
                dao.insertChannelList(listOf())
            } just runs

            coEvery {
                dao.deleteChannels()
            } just runs


            withClue("single call from epg execute") {
                repository.loadProgramFromSource()
                coVerify(exactly = 1) {
                    epg.getChannels()
                }
                confirmVerified(epg)
            }

            withClue("proper calls from dao execute") {
                // todo find how test run on condition
                repository.updateEntities(listOf())
                coVerifySequence {
                    dao.deleteChannels()
                    dao.insertChannelList(listOf())
                }
            }
        }

        "loadChannels verify calls" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<AllChannelDao>(relaxed = true)
            val repository = AllChannelRepository(epg, dao)

            withClue("proper calls from epg and dao execute") {
                repository.loadChannels()

                coVerifySequence {
                    dao.getChannelList()
                    epg.getChannels()
                    dao.insertChannelList(listOf())
                }
            }


        }

        "loadChannels returns proper data" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<AllChannelDao>(relaxed = true)
            val repository = AllChannelRepository(epg, dao)

            val expectedResultDao = listOf(
                AvailableChannelEntity("id1", "name1", "icon"),
                AvailableChannelEntity("id2", "name2", "icon"),
                AvailableChannelEntity("id3", "name3", "icon")
            )

            val expectedResult = listOf(
                AvailableChannel("id1", "name1", "icon", false),
                AvailableChannel("id2", "name2", "icon", false),
                AvailableChannel("id3", "name3", "icon", false),
            )

            coEvery {
                dao.getChannelList()
            } returns expectedResultDao

            val retrievedResult = repository.loadChannels()

            withClue("single call from dao execute") {
                coVerify(exactly = 1) {
                    dao.getChannelList()
                }
                confirmVerified(dao)
            }

            withClue("result is list of string value") {
                retrievedResult.shouldBeInstanceOf<List<AvailableChannel>>()
                retrievedResult.first() shouldBeEqualToComparingFields expectedResult.first()
            }

            withClue("result elements proper count") {
                retrievedResult.count() shouldBe expectedResult.count()
            }

            withClue("result fields values match expected values first item") {
                retrievedResult.first().channelName shouldBe expectedResult.first().channelName
                retrievedResult.first().channelId shouldBe expectedResult.first().channelId
            }

            withClue("result fields values match expected values last item") {
                retrievedResult.last().channelName shouldBe "name3"
                retrievedResult.last().channelId shouldBe "id3"
            }
        }
    }
})