package com.mvproject.tvprogramguide.mapper

import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asChannelLists
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toAvailableChannelEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toChannelsListEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class MappersTest : FunSpec({

    context("AvailableChannelResponse to Entity mapping") {
        test("toAvailableChannelEntities maps correctly") {
            val response1 = mockk<AvailableChannelResponse> {
                every { channelNames } returns "Channel 1"
                every { channelIcon } returns "icon1.png"
                every { channelId } returns "id1"
            }
            val response2 = mockk<AvailableChannelResponse> {
                every { channelNames } returns "Channel 2"
                every { channelIcon } returns "icon2.png"
                every { channelId } returns "id2"
            }

            val result = listOf(response1, response2).toAvailableChannelEntities()

            result.size shouldBe 2
            withClue("First item should be mapped correctly") {
                result[0].title shouldBe "Channel 1"
                result[0].logo shouldBe "icon1.png"
                result[0].programId shouldBe "id1"
                result[0].id shouldBe "id1Channel1"
            }
            withClue("Second item should be mapped correctly") {
                result[1].title shouldBe "Channel 2"
                result[1].logo shouldBe "icon2.png"
                result[1].programId shouldBe "id2"
                result[1].id shouldBe "id2Channel2"
            }
        }
    }

    context("ProgramEntity to Program mapping") {
        test("asProgramFromEntities maps correctly") {
            val entity1 = ProgramEntity(
                programId = "prog1",
                dateTimeStart = 1000L,
                dateTimeEnd = 2000L,
                title = "Program 1",
                description = "Desc 1",
                category = "Cat 1",
                channelId = "channel1",
                scheduledId = null
            )
            val entity2 = ProgramEntity(
                programId = "prog2",
                dateTimeStart = 2000L,
                dateTimeEnd = 3000L,
                title = "Program 2",
                description = "Desc 2",
                category = "Cat 2",
                channelId = "channel2",
                scheduledId = 123L
            )

            val result = listOf(entity1, entity2).asProgramFromEntities()

            result.size shouldBe 2
            withClue("First program should be mapped correctly") {
                result[0].programId shouldBe "prog1"
                result[0].dateTimeStart shouldBe 1000L
                result[0].dateTimeEnd shouldBe 2000L
                result[0].title shouldBe "Program 1"
                result[0].description shouldBe "Desc 1"
                result[0].category shouldBe "Cat 1"
                result[0].channel shouldBe "channel1"
                result[0].scheduledId shouldBe null
            }
            withClue("Second program should be mapped correctly") {
                result[1].programId shouldBe "prog2"
                result[1].dateTimeStart shouldBe 2000L
                result[1].dateTimeEnd shouldBe 3000L
                result[1].title shouldBe "Program 2"
                result[1].description shouldBe "Desc 2"
                result[1].category shouldBe "Cat 2"
                result[1].channel shouldBe "channel2"
                result[1].scheduledId shouldBe 123L
            }
        }
    }

    context("ChannelsListEntity to ChannelList mapping") {
        test("asChannelLists maps correctly") {
            val entity1 = ChannelsListEntity(id = 1, name = "List 1", isSelected = true)
            val entity2 = ChannelsListEntity(id = 2, name = "List 2", isSelected = false)

            val result = listOf(entity1, entity2).asChannelLists()

            result.size shouldBe 2
            withClue("First channel list should be mapped correctly") {
                result[0].id shouldBe 1
                result[0].listName shouldBe "List 1"
                result[0].isSelected shouldBe true
            }
            withClue("Second channel list should be mapped correctly") {
                result[1].id shouldBe 2
                result[1].listName shouldBe "List 2"
                result[1].isSelected shouldBe false
            }
        }
    }

    context("ChannelList to ChannelsListEntity mapping") {
        test("toChannelsListEntity maps correctly") {
            val channelList = ChannelList(id = 1, listName = "List 1", isSelected = true)

            val result = channelList.toChannelsListEntity()

            withClue("ChannelList should be mapped correctly to ChannelsListEntity") {
                result.id shouldBe 1
                result.name shouldBe "List 1"
                result.isSelected shouldBe true
            }
        }
    }
})