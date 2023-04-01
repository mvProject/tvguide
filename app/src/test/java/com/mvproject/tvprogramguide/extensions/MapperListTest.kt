package com.mvproject.tvprogramguide.extensions

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.Mappers.toSelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.Mappers.toSingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.convertDateToReadableFormat
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class MapperListTest : ShouldSpec({

    context("sort and map programs of single channel by dates") {
        val time = Clock.System.now()

        val sourceList = listOf(
            Program(
                time.toEpochMilliseconds(),
                (time + 1.hours).toEpochMilliseconds(),
                "title 1"
            ),
            Program(
                (time + 1.hours).toEpochMilliseconds(),
                (time + 2.hours).toEpochMilliseconds(),
                "title 2"
            ),
            Program(
                (time + 2.hours).toEpochMilliseconds(),
                (time + 3.hours).toEpochMilliseconds(),
                "title 3"
            ),
            Program(
                (time + 25.hours).toEpochMilliseconds(),
                (time + 26.hours).toEpochMilliseconds(),
                "title 4"
            ),
            Program(
                (time + 26.hours).toEpochMilliseconds(),
                (time + 27.hours).toEpochMilliseconds(),
                "title 5"
            )
        )
        val expectedList = listOf(
            SingleChannelWithPrograms(
                date = time.toEpochMilliseconds().convertDateToReadableFormat(),
                programs = listOf(
                    Program(
                        time.toEpochMilliseconds(),
                        (time + 1.hours).toEpochMilliseconds(),
                        "title 1"
                    ),
                    Program(
                        (time + 1.hours).toEpochMilliseconds(),
                        (time + 2.hours).toEpochMilliseconds(),
                        "title 2"
                    ),
                    Program(
                        (time + 2.hours).toEpochMilliseconds(),
                        (time + 3.hours).toEpochMilliseconds(),
                        "title 3"
                    )
                )
            ),
            SingleChannelWithPrograms(
                date = (time + 25.hours).toEpochMilliseconds().convertDateToReadableFormat(),
                programs = listOf(
                    Program(
                        (time + 25.hours).toEpochMilliseconds(),
                        (time + 26.hours).toEpochMilliseconds(),
                        "title 4"
                    ),
                    Program(
                        (time + 26.hours).toEpochMilliseconds(),
                        (time + 27.hours).toEpochMilliseconds(),
                        "title 5"
                    ),
                )
            ),
        )

        val retrievedList = sourceList.toSingleChannelWithPrograms()
        assertSoftly {
            should("result is list of string value") {
                retrievedList.shouldBeInstanceOf<List<SingleChannelWithPrograms>>()
                retrievedList.first() shouldBeEqualToComparingFields expectedList.first()
            }

            should("result elements proper count") {
                retrievedList.count() shouldBe expectedList.count()
                retrievedList.first().programs.count() shouldBe expectedList.first().programs.count()
                retrievedList.last().programs.count() shouldBe expectedList.last().programs.count()
            }

            should("result fields values match expected values first items") {
                retrievedList.first().date shouldBe expectedList.first().date
                retrievedList.first().programs.first().title shouldBe expectedList.first().programs.first().title
            }

            should("result fields values match expected values last items") {
                retrievedList.last().date shouldBe expectedList.last().date
                retrievedList.last().programs.first().title shouldBe "title 4"
                retrievedList.last().programs.last().title shouldBe "title 5"
            }
        }
    }

    context("sort and map programs by channels with specified default count") {
        val time = Clock.System.now()

        val sourceList = listOf(
            Program(
                time.toEpochMilliseconds(),
                (time + 1.hours).toEpochMilliseconds(),
                "title 1",
                channel = "channel1"
            ),
            Program(
                (time + 1.hours).toEpochMilliseconds(),
                (time + 2.hours).toEpochMilliseconds(),
                "title 2",
                channel = "channel1"
            ),
            Program(
                (time + 2.hours).toEpochMilliseconds(),
                (time + 3.hours).toEpochMilliseconds(),
                "title 3",
                channel = "channel2"
            ),
            Program(
                (time + 25.hours).toEpochMilliseconds(),
                (time + 26.hours).toEpochMilliseconds(),
                "title 4",
                channel = "channel1"
            ),
            Program(
                (time + 26.hours).toEpochMilliseconds(),
                (time + 27.hours).toEpochMilliseconds(),
                "title 5",
                channel = "channel3"
            ),
            Program(
                (time + 27.hours).toEpochMilliseconds(),
                (time + 28.hours).toEpochMilliseconds(),
                "title 6",
                channel = "channel2"
            )
        )
        val expectedList = listOf(
            SelectedChannelWithPrograms(
                selectedChannel = SelectedChannel("channel1", "channelName1", "icon"),
                programs = listOf(
                    Program(
                        time.toEpochMilliseconds(),
                        (time + 1.hours).toEpochMilliseconds(),
                        "title 1",
                        channel = "channel1"
                    ),
                    Program(
                        (time + 1.hours).toEpochMilliseconds(),
                        (time + 2.hours).toEpochMilliseconds(),
                        "title 2",
                        channel = "channel1"
                    ),
                    Program(
                        (time + 25.hours).toEpochMilliseconds(),
                        (time + 26.hours).toEpochMilliseconds(),
                        "title 4",
                        channel = "channel1"
                    )
                )
            ),
            SelectedChannelWithPrograms(
                selectedChannel = SelectedChannel("channel2", "channelName2", "icon"),
                programs = listOf(
                    Program(
                        (time + 2.hours).toEpochMilliseconds(),
                        (time + 3.hours).toEpochMilliseconds(),
                        "title 3",
                        channel = "channel2"
                    ),
                    Program(
                        (time + 27.hours).toEpochMilliseconds(),
                        (time + 28.hours).toEpochMilliseconds(),
                        "title 6",
                        channel = "channel2"
                    ),
                )
            ),
        )

        val selectedChannelsWithCount = listOf(
            SelectedChannel("channel1", "channelName1", "icon"),
            SelectedChannel("channel2", "channelName2", "icon")
        )

        val retrievedList = sourceList.toSelectedChannelWithPrograms(
            selectedChannelsWithCount,
            2
        )
        assertSoftly {
            should("result is list of string value") {
                retrievedList.shouldBeInstanceOf<List<SelectedChannelWithPrograms>>()
                retrievedList.first() shouldBeEqualToComparingFields expectedList.first()
            }

            should("result elements proper count") {
                retrievedList.count() shouldBe expectedList.count()
                retrievedList.first().programs.count() shouldBe 2
                retrievedList.last().programs.count() shouldBe expectedList.last().programs.count()
            }

            should("result fields values match expected values first items") {
                retrievedList.first().selectedChannel.channelName shouldBe expectedList.first().selectedChannel.channelName
                retrievedList.first().programs.first().title shouldBe expectedList.first().programs.first().title
            }

            should("result fields values match expected values last items") {
                retrievedList.last().selectedChannel.channelName shouldBe expectedList.last().selectedChannel.channelName
                retrievedList.last().programs.first().title shouldBe "title 3"
                retrievedList.last().programs.last().title shouldBe "title 6"
            }
        }
    }

    context("sort and map programs by channels with empty default count") {
        val time = Clock.System.now()

        val sourceList = listOf(
            Program(
                time.toEpochMilliseconds(),
                (time + 1.hours).toEpochMilliseconds(),
                "title 1",
                channel = "channel1"
            ),
            Program(
                (time + 1.hours).toEpochMilliseconds(),
                (time + 2.hours).toEpochMilliseconds(),
                "title 2",
                channel = "channel1"
            ),
            Program(
                (time + 2.hours).toEpochMilliseconds(),
                (time + 3.hours).toEpochMilliseconds(),
                "title 3",
                channel = "channel2"
            ),
            Program(
                (time + 25.hours).toEpochMilliseconds(),
                (time + 26.hours).toEpochMilliseconds(),
                "title 4",
                channel = "channel1"
            ),
            Program(
                (time + 26.hours).toEpochMilliseconds(),
                (time + 27.hours).toEpochMilliseconds(),
                "title 5",
                channel = "channel3"
            ),
            Program(
                (time + 27.hours).toEpochMilliseconds(),
                (time + 28.hours).toEpochMilliseconds(),
                "title 6",
                channel = "channel2"
            )
        )
        val expectedList = listOf(
            SelectedChannelWithPrograms(
                selectedChannel = SelectedChannel("channel1", "channelName1", "icon"),
                programs = listOf(
                    Program(
                        time.toEpochMilliseconds(),
                        (time + 1.hours).toEpochMilliseconds(),
                        "title 1",
                        channel = "channel1"
                    ),
                    Program(
                        (time + 1.hours).toEpochMilliseconds(),
                        (time + 2.hours).toEpochMilliseconds(),
                        "title 2",
                        channel = "channel1"
                    ),
                    Program(
                        (time + 25.hours).toEpochMilliseconds(),
                        (time + 26.hours).toEpochMilliseconds(),
                        "title 4",
                        channel = "channel1"
                    )
                )
            ),
            SelectedChannelWithPrograms(
                SelectedChannel("channel3", "channelName3", "icon"),
                programs = listOf(
                    Program(
                        (time + 26.hours).toEpochMilliseconds(),
                        (time + 27.hours).toEpochMilliseconds(),
                        "title 5",
                        channel = "channel3"
                    )
                )
            ),
        )

        val selectedChannelsWithCount = listOf(
            SelectedChannel("channel1", "channelName1", "icon"),
            SelectedChannel("channel3", "channelName3", "icon")
        )

        val retrievedList = sourceList.toSelectedChannelWithPrograms(
            selectedChannelsWithCount,
            0
        )
        assertSoftly {
            should("result is list of string value") {
                retrievedList.shouldBeInstanceOf<List<SelectedChannelWithPrograms>>()
                retrievedList.first() shouldBeEqualToComparingFields expectedList.first()
            }

            should("result elements proper count") {
                retrievedList.count() shouldBe expectedList.count()
                retrievedList.first().programs.count() shouldBe expectedList.first().programs.count()
                retrievedList.last().programs.count() shouldBe expectedList.last().programs.count()
            }

            should("result fields values match expected values first items") {
                retrievedList.first().selectedChannel.channelName shouldBe expectedList.first().selectedChannel.channelName
                retrievedList.first().programs.first().title shouldBe expectedList.first().programs.first().title
            }

            should("result fields values match expected values last items") {
                retrievedList.last().selectedChannel.channelName shouldBe expectedList.last().selectedChannel.channelName
                retrievedList.last().programs.first().title shouldBe "title 5"
                retrievedList.last().programs.last().title shouldBe "title 5"
            }
        }
    }

})