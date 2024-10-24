package com.mvproject.tvprogramguide.utils


import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.ProgramUtils.toSelectedChannelWithPrograms
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProgramUtilsTest : FunSpec({

    context("toSelectedChannelWithPrograms") {
        val program1 = Program(
            programId = "1",
            title = "Program 1",
            channel = "channel1",
            dateTimeStart = 0L,
            dateTimeEnd = 0L
        )
        val program2 = Program(
            programId = "2",
            title = "Program 2",
            channel = "channel1",
            dateTimeStart = 0L,
            dateTimeEnd = 0L
        )
        val program3 = Program(
            programId = "3",
            title = "Program 3",
            channel = "channel2",
            dateTimeStart = 0L,
            dateTimeEnd = 0L
        )

        val channel1 = SelectionChannel(
            channelId = "1",
            programId = "channel1",
            channelName = "Channel 1",
            order = 1
        )
        val channel2 = SelectionChannel(
            channelId = "2",
            programId = "channel2",
            channelName = "Channel 2",
            order = 2
        )
        val channel3 = SelectionChannel(
            channelId = "3",
            programId = "channel3",
            channelName = "Channel 3",
            order = 3
        )

        test("should correctly group programs by channel") {
            val programs = listOf(program1, program2, program3)
            val selectedChannels = listOf(channel1, channel2, channel3)

            val result = programs.toSelectedChannelWithPrograms(selectedChannels)

            result shouldHaveSize 3

            withClue("Channel 1 should have 2 programs") {
                result[0].selectedChannel shouldBe channel1
                result[0].programs shouldHaveSize 2
            }

            withClue("Channel 2 should have 1 program") {
                result[1].selectedChannel shouldBe channel2
                result[1].programs shouldHaveSize 1
            }

            withClue("Channel 3 should have no programs") {
                result[2].selectedChannel shouldBe channel3
                result[2].programs.shouldBeEmpty()
            }
        }

        test("should limit the number of programs when itemsCount is specified") {
            val programs = listOf(program1, program2, program3)
            val selectedChannels = listOf(channel1, channel2)

            val result = programs.toSelectedChannelWithPrograms(selectedChannels, itemsCount = 1)

            result shouldHaveSize 2

            withClue("Each channel should have at most 1 program") {
                result.forEach { channelWithPrograms ->
                    channelWithPrograms.programs shouldHaveSize 1
                }
            }
        }

        test("should handle empty program list") {
            val programs = emptyList<Program>()
            val selectedChannels = listOf(channel1, channel2)

            val result = programs.toSelectedChannelWithPrograms(selectedChannels)

            result shouldHaveSize 2

            withClue("All channels should have empty program lists") {
                result.forEach { channelWithPrograms ->
                    channelWithPrograms.programs.shouldBeEmpty()
                }
            }
        }

        test("should handle empty selected channels list") {
            val programs = listOf(program1, program2, program3)
            val selectedChannels = emptyList<SelectionChannel>()

            val result = programs.toSelectedChannelWithPrograms(selectedChannels)

            result.shouldBeEmpty()
        }

        test("should handle mismatched channel IDs") {
            val programs = listOf(program1, program2, program3)
            val mismatchedChannel = SelectionChannel(
                channelId = "4",
                programId = "channel4",
                channelName = "Channel 4",
                order = 4
            )
            val selectedChannels = listOf(mismatchedChannel)

            val result = programs.toSelectedChannelWithPrograms(selectedChannels)

            result shouldHaveSize 1

            withClue("Mismatched channel should have no programs") {
                result[0].selectedChannel shouldBe mismatchedChannel
                result[0].programs.shouldBeEmpty()
            }
        }
    }
})