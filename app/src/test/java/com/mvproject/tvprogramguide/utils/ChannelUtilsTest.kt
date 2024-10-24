package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.ChannelUtils.updateOrders
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ChannelUtilsTest : FunSpec({

    context("ChannelUtils.updateOrders") {
        test("should return an empty list when given an empty list") {
            val emptyList = emptyList<SelectionChannel>()
            val result = emptyList.updateOrders()
            result.shouldBeEmpty()
        }

        test("should correctly update orders for a non-empty list") {
            val channels = listOf(
                SelectionChannel(channelId = "1", programId = "p1", channelName = "Channel 1", order = 0),
                SelectionChannel(channelId = "2", programId = "p2", channelName = "Channel 2", order = 0),
                SelectionChannel(channelId = "3", programId = "p3", channelName = "Channel 3", order = 0)
            )

            val result =  channels.updateOrders()

            result.shouldHaveSize(3)

            withClue("Each channel should have its order updated") {
                result.forEachIndexed { index, channel ->
                    channel.order shouldBe index + 1
                }
            }
        }

        test("should handle a list with pre-existing orders") {
            val channels = listOf(
                SelectionChannel(channelId = "1", programId = "p1", channelName = "Channel 1", order = 5),
                SelectionChannel(channelId = "2", programId = "p2", channelName = "Channel 2", order = 3),
                SelectionChannel(channelId = "3", programId = "p3", channelName = "Channel 3", order = 1)
            )

            val result = channels.updateOrders()

            result.shouldHaveSize(3)

            withClue("Each channel should have its order updated regardless of pre-existing order") {
                result.forEachIndexed { index, channel ->
                    channel.order shouldBe index + 1
                }
            }
        }

        test("should handle a list with a single item") {
            val singleChannel = listOf(
                SelectionChannel(channelId = "1", programId = "p1", channelName = "Channel 1", order = 0),
            )

            val result = singleChannel.updateOrders()

            result.shouldHaveSize(1)
            result[0].order shouldBe 1
        }
    }
})