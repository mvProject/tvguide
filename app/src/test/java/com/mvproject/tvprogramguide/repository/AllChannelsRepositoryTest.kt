package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class AllChannelsRepositoryTest : StringSpec({

    assertSoftly {
        "loadProgramFromSource verify calls" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<AllChannelDao>(relaxed = true)
            val repository = AllChannelRepository(epg, dao)

            repository.loadProgramFromSource()
            coVerify(exactly = 1) {
                epg.getChannels()
            }

            // todo find how test run on condition
            repository.updateEntities(listOf())
            coVerify(atMost = 1) {
                dao.insertChannelList(listOf())
            }

        }

        "loadChannels verify calls" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<AllChannelDao>(relaxed = true)
            val repository = AllChannelRepository(epg, dao)

            repository.loadChannels()

            coVerify(exactly = 1) {
                dao.getChannelList()
                epg.getChannels()
                dao.insertChannelList(listOf())
            }
        }

        "loadChannels returns proper data" {
            val epg = mockk<EpgService>(relaxed = true)
            val dao = mockk<AllChannelDao>(relaxed = true)
            val repository = AllChannelRepository(epg, dao)

            val resultListEntity = listOf(
                AvailableChannelEntity("id", "name", "icon")
            )

            val resultList = listOf(
                AvailableChannel("id", "name", "icon", false)
            )

            coEvery {
                dao.getChannelList()
            } returns resultListEntity

            val result = repository.loadChannels()

            result.shouldBeInstanceOf<List<AvailableChannel>>()
            result.first() shouldBeEqualToComparingFields resultList.first()
            result.first().channelName shouldBe "name"
            result.first().channelId shouldBe "id"

        }
    }
})