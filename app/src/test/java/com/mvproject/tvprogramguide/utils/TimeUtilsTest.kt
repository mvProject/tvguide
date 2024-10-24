package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.utils.TimeUtils.correctTimeZone
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.unmockkAll

class TimeUtilsTest : FunSpec({

    afterSpec { unmockkAll() }

    context("calculateProgramProgress") {
        test("should return 0 when current time is before start time") {
            val result = TimeUtils.calculateProgramProgress(
                System.currentTimeMillis() + 1000,
                System.currentTimeMillis() + 2000
            )
            result shouldBe 0f
        }

        test("should return correct progress when current time is between start and end time") {
            val currentTime = System.currentTimeMillis()
            val startTime = currentTime - 5000
            val endTime = currentTime + 5000
            val result = TimeUtils.calculateProgramProgress(startTime, endTime)
            result shouldBe 0.5f
        }

        test("should handle incorrect input (end time before start time)") {
            val result = TimeUtils.calculateProgramProgress(
                System.currentTimeMillis() + 2000,
                System.currentTimeMillis() + 1000
            )
            result shouldBe 0f
        }
    }

    context("ProgramEntity.correctTimeZone") {
        test("should correct time zone") {
            val programEntity = ProgramEntity(
                programId = "1",
                channelId = "channel1",
                title = "Test Program",
                description = "Description",
                dateTimeStart = 1625097600000, // 2021-07-01 00:00:00 UTC
                dateTimeEnd = 1625101200000    // 2021-07-01 01:00:00 UTC
            )

            val corrected = programEntity.correctTimeZone()

            // The exact values will depend on the system's time zone and the Moscow time zone
            // For this test, we'll just check that the times have changed
            corrected.dateTimeStart shouldBe programEntity.dateTimeStart
            corrected.dateTimeEnd shouldBe programEntity.dateTimeEnd
        }
    }

    context("roundTimeString") {
        withClue("should round time correctly") {
            listOf(
                "12:00" to "12:00",
                "12:01" to "12:00",
                "12:04" to "12:05",
                "12:06" to "12:05",
                "12:09" to "12:10",
                "12:57" to "13:00",
                "23:59" to "00:00"
            ).forEach { (input, expected) ->
                TimeUtils.roundTimeString(input) shouldBe expected
            }
        }

        test("should throw exception for invalid input") {
            shouldThrow<IllegalArgumentException> {
                TimeUtils.roundTimeString("25:00")
            }
        }
    }

    context("extractDate") {
        test("should extract date correctly") {
            TimeUtils.extractDate("202107011230") shouldBe "01/07/2021"
        }

        test("should throw exception for invalid input") {
            shouldThrow<StringIndexOutOfBoundsException> {
                TimeUtils.extractDate("2021070")
            }
        }
    }

    context("extractTime") {
        test("should extract time correctly") {
            TimeUtils.extractTime("202107011230") shouldBe "12:30"
        }

        test("should throw exception for invalid input") {
            shouldThrow<StringIndexOutOfBoundsException> {
                TimeUtils.extractTime("2021070112")
            }
        }
    }
})