package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.utils.AppConstants.USER_LIST_MAX_LENGTH
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExtensionsTest : FunSpec({
    val testDateLong = 1617235200000
    val testDateShort = "01 04 2021"

    context("Long.convertDateToReadableFormat") {
        test("converts epoch milliseconds to readable date format") {
            withClue("for a valid date") {
                testDateLong.convertDateToReadableFormat() shouldBe testDateShort
            }

            withClue("for epoch start") {
                val millis = 0L // 1970-01-01 00:00:00 UTC
                millis.convertDateToReadableFormat() shouldBe "01 01 1970"
            }

            withClue("parse long date not empty or some") {
                testDateLong.convertDateToReadableFormat() shouldNotBe ""
                testDateLong.convertDateToReadableFormat() shouldNotBe "Some String"
            }
            withClue("parse zero long to date not empty") {
                0L.convertDateToReadableFormat() shouldNotBe ""
            }

            withClue("parse not date long to date not empty") {
                1000L.convertDateToReadableFormat() shouldNotBe ""
            }
        }
    }

    context("Long.convertDateToReadableFormat2") {
        test("converts epoch milliseconds to readable date format") {
            withClue("for a valid date") {
                testDateLong.convertDateToReadableFormat2() shouldBe testDateShort
            }

            withClue("for epoch start") {
                val millis = 0L // 1970-01-01 00:00:00 UTC
                millis.convertDateToReadableFormat2() shouldBe "01 01 1970"
            }

            withClue("parse long date not empty or some") {
                testDateLong.convertDateToReadableFormat2() shouldNotBe ""
                testDateLong.convertDateToReadableFormat2() shouldNotBe "Some String"
            }
            withClue("parse zero long to date not empty") {
                0L.convertDateToReadableFormat2() shouldNotBe ""
            }

            withClue("parse not date long to date not empty") {
                1000L.convertDateToReadableFormat2() shouldNotBe ""
            }
        }
    }

    context("Long.convertTimeToReadableFormat") {
        test("converts epoch milliseconds to readable time format") {
            withClue("for a valid time") {
                val millis = 1617279296000 // 2021-04-01 12:34:56 UTC
                val result = millis.convertTimeToReadableFormat()

                // Use regex to match the time format HH:mm:ss
                result shouldMatch "\\d{2}:\\d{2}:\\d{2}".toRegex()

                // Verify the converted time is correct
                val instant = Instant.fromEpochMilliseconds(millis)
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                result shouldBe localDateTime.time.toString()
            }
        }
    }

    context("List<String>.obtainIndexOrZero") {
        test("returns correct index for existing element") {
            val list = listOf("apple", "banana", "cherry")
            list.obtainIndexOrZero("banana") shouldBe 1
        }

        test("returns zero for non-existing element") {
            val list = listOf("apple", "banana", "cherry")
            list.obtainIndexOrZero("grape") shouldBe 0
        }

        test("returns zero for empty list") {
            val list = emptyList<String>()
            list.obtainIndexOrZero("apple") shouldBe 0
        }
    }

    context("List<T>.takeIfCountNotEmpty") {
        test("returns original list when count is zero or negative") {
            val list = listOf(1, 2, 3, 4, 5)
            list.takeIfCountNotEmpty(0) shouldBe list
            list.takeIfCountNotEmpty(-1) shouldBe list
        }

        test("returns original list when count is greater than or equal to list size") {
            val list = listOf(1, 2, 3, 4, 5)
            list.takeIfCountNotEmpty(5) shouldBe list
            list.takeIfCountNotEmpty(6) shouldBe list
        }

        test("returns sublist when count is positive and less than list size") {
            val list = listOf(1, 2, 3, 4, 5)
            list.takeIfCountNotEmpty(3) shouldBe listOf(1, 2, 3)
        }
    }

    context("String.manageLength") {
        test("returns original string when length is less than or equal to USER_LIST_MAX_LENGTH") {
            val shortString = "Short"
            shortString.manageLength() shouldBe shortString
        }

        test("truncates string when length is greater than USER_LIST_MAX_LENGTH") {
            val longString = "A".repeat(USER_LIST_MAX_LENGTH + 10)
            longString.manageLength() shouldBe "A".repeat(USER_LIST_MAX_LENGTH)
        }
    }

    context("String.trimSpaces") {
        test("removes all spaces from string") {
            "Hello World".trimSpaces() shouldBe "HelloWorld"
            "  Spaces  Everywhere  ".trimSpaces() shouldBe "SpacesEverywhere"
            "NoSpaces".trimSpaces() shouldBe "NoSpaces"
            "".trimSpaces() shouldBe ""
        }
    }
})