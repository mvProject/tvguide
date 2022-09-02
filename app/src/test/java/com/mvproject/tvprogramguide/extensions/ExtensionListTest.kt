package com.mvproject.tvprogramguide.extensions

import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_EPG_PROGRAM_TITLE
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.data.utils.getNoProgramData
import com.mvproject.tvprogramguide.data.utils.obtainIndexOrZero
import com.mvproject.tvprogramguide.data.utils.takeIfCountNotEmpty
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldNotBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class ExtensionListTest : ShouldSpec({
    val properTarget = "Target"
    val wrongTarget = "TargetWrong"
    val targetList = listOf("TargetFirst", "Target", "TargetSecond", "TargetThird")
    val wrongTargetList = listOf("First", "Tar", "Get", "Target Second", "Target Third")

    val listName = "Test"
    val emptyListName = ""

    val takeCount = 5
    val emptyTakeCount = 0
    val largeList = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    val shortList = listOf(1, 2, 3, 4)


    context("index of target string in list") {
        assertSoftly {
            should("return the index of proper target") {
                targetList.obtainIndexOrZero(properTarget) shouldBe 1
            }

            should("return the index of wrong target") {
                targetList.obtainIndexOrZero(wrongTarget) shouldBe 0
                wrongTargetList.obtainIndexOrZero(properTarget) shouldBe 0
                wrongTargetList.obtainIndexOrZero(wrongTarget) shouldBe 0
            }

            should("return the index fo wrong target non negative") {
                targetList.obtainIndexOrZero(properTarget) shouldNotBeLessThan 0
                targetList.obtainIndexOrZero(wrongTarget) shouldNotBeLessThan 0
                wrongTargetList.obtainIndexOrZero(properTarget) shouldNotBeLessThan 0
                wrongTargetList.obtainIndexOrZero(wrongTarget) shouldNotBeLessThan 0
            }
        }
    }

    context("list with default values") {
        assertSoftly {
            should("should return result with proper items count") {
                listName.getNoProgramData().count() shouldBe 5
            }

            should("should proper result type") {
                listName.getNoProgramData() shouldBeSameInstanceAs listOf<ProgramEntity>()
                listName.getNoProgramData().first() shouldBeSameInstanceAs ProgramEntity::class
            }

            should("should return result with proper items count") {
                listName.getNoProgramData().first().title shouldBe NO_EPG_PROGRAM_TITLE
                listName.getNoProgramData().first().description shouldBe NO_VALUE_STRING
                listName.getNoProgramData().first().channelId shouldBe listName
            }

            should("should with empty list name return result with proper items count") {
                emptyListName.getNoProgramData().first().title shouldBe NO_EPG_PROGRAM_TITLE
                emptyListName.getNoProgramData().first().description shouldBe NO_VALUE_STRING
                emptyListName.getNoProgramData().first().channelId shouldBe emptyListName
            }
        }
    }

    context("take proper count from list") {
        assertSoftly {
            should("should return result with proper items when take is too big") {
                largeList.takeIfCountNotEmpty(takeCount) shouldBe listOf(1, 2, 3, 4, 5)
            }

            should("should return result with proper items when take empty") {
                largeList.takeIfCountNotEmpty(emptyTakeCount) shouldBe largeList
            }

            should("should return result from small with proper items when take is too big") {
                shortList.takeIfCountNotEmpty(takeCount) shouldBe takeCount
            }

            should("should return result from small with proper items when take empty") {
                shortList.takeIfCountNotEmpty(emptyTakeCount) shouldBe shortList
            }

            should("should return result with proper items count") {
                largeList.takeIfCountNotEmpty(takeCount).count() shouldBe 5
                largeList.takeIfCountNotEmpty(emptyTakeCount).count() shouldBe 8
            }

            should("should return result from small with proper items count when take is too big") {
                shortList.takeIfCountNotEmpty(takeCount).count() shouldBe 4
                shortList.takeIfCountNotEmpty(emptyTakeCount).count() shouldBe 4
            }
        }
    }
})