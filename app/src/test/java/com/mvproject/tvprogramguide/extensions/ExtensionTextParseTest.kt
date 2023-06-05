package com.mvproject.tvprogramguide.extensions

import com.mvproject.tvprogramguide.data.utils.manageLength
import com.mvproject.tvprogramguide.data.utils.parseChannelName
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExtensionTextParseTest : StringSpec({
    val chnOneDelimiter = "PartOne • PartOne"
    val chnTwoDelimiter = "PartOne • PartOne • PartOne"
    val chnOtherDelimiter = "PartOne * PartOne"
    val chnNoDelimiter = "PartOne PartOne"

    val longName = "SomeVeryVeryLongNameForSomething"
    val shortName = "SomeShortName"
    val emptyName = ""

    "delimiterTest" {
        withClue("channel name parse") {
            withClue("no delimiter") {
                chnNoDelimiter.parseChannelName() shouldBe chnNoDelimiter
            }
            withClue("not specified delimiter") {
                chnOtherDelimiter.parseChannelName() shouldBe chnOtherDelimiter
            }
            withClue("with one delimiter") {
                chnOneDelimiter.parseChannelName() shouldBe "PartOne"
            }
            withClue("with two delimiters") {
                chnTwoDelimiter.parseChannelName() shouldBe "PartOne"
            }
            withClue("empty name") {
                "".parseChannelName() shouldBe ""
            }
        }
    }

    "manage string length" {
        withClue("some name parse") {
            withClue("manage empty name") {
                emptyName.manageLength() shouldBe emptyName
                emptyName.manageLength().length shouldBe 0
            }
            withClue("manage shortName") {
                shortName.manageLength() shouldBe shortName
                shortName.manageLength().length shouldBe 13
            }
            withClue("manage LongName") {
                longName.manageLength() shouldBe "SomeVeryVeryLongName"
                longName.manageLength().length shouldBe 20
            }
        }
    }
})
