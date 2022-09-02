package com.mvproject.tvprogramguide.extensions

import com.mvproject.tvprogramguide.data.utils.convertDateToReadableFormat
import com.mvproject.tvprogramguide.data.utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.data.utils.toMillis
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldNotBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ExtensionDateParseTest : StringSpec({
    val testDateText = "29-08-2022 12:15"
    val testDateLong = 1661764500000L
    val testDateShort = "29 08 2022"
    val testTimeShort = "12:15"

    "textDateParseTest" {
        withClue("text date parse") {
            withClue("parse date to long") {
                testDateText.toMillis() shouldBe testDateLong
            }
            withClue("parse date to not non positive long") {
                testDateText.toMillis() shouldNotBeLessThanOrEqual 0L
            }
            withClue("parse date to non zero long") {
                testDateText.toMillis() shouldBeGreaterThan 0L
            }
        }
    }

    "longDateParseTest" {
        withClue("long date parse to date") {
            withClue("parse long date to short text") {
                testDateLong.convertDateToReadableFormat() shouldBe testDateShort
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

        withClue("long date parse to time") {
            withClue("parse long date to short text") {
                testDateLong.convertTimeToReadableFormat() shouldBe testTimeShort
            }
            withClue("parse long date not empty or some") {
                testDateLong.convertTimeToReadableFormat() shouldNotBe ""
                testDateLong.convertTimeToReadableFormat() shouldNotBe "Some String"
            }
            withClue("parse zero long to time not empty") {
                0L.convertTimeToReadableFormat() shouldNotBe ""
            }

            withClue("parse not date long to time not empty") {
                1000L.convertTimeToReadableFormat() shouldNotBe ""
            }
        }
    }
})