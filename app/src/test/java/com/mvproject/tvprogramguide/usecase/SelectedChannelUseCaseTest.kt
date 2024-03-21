package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SelectedChannelUseCaseTest : StringSpec({

    lateinit var preferenceRepository: PreferenceRepository
    lateinit var selectedChannelRepository: SelectedChannelRepository

    lateinit var selectedChannelUseCase: SelectedChannelUseCase

    beforeTest {
        preferenceRepository = createPreferenceMockRepository()
        selectedChannelRepository = createSelectedChannelMockRepository()

        selectedChannelUseCase =
            SelectedChannelUseCase(
                selectedChannelRepository,
                preferenceRepository,
            )
    }

    afterTest {
        println("test ${it.a.name.testName} complete status is ${it.b.isSuccess}")
    }

    assertSoftly {
        "load selected channels with flow" {
            val expectedResult =
                listOf(
                    SelectedChannel(
                        "testId1",
                        "testName1",
                        "iconUrl",
                        order = 1,
                        parentList = "test",
                    ),
                    SelectedChannel(
                        "testId2",
                        "testName2",
                        "iconUrl",
                        order = 2,
                        parentList = "test",
                    ),
                    SelectedChannel(
                        "testId3",
                        "testName3",
                        "iconUrl",
                        order = 3,
                        parentList = "test",
                    ),
                )

            withClue("single call from selectedChannelRepository execute") {
                selectedChannelUseCase.loadSelectedChannelsFlow()

                coVerify(exactly = 1) {
                    selectedChannelRepository.loadSelectedChannelsFlow("test")
                }
                confirmVerified(selectedChannelRepository)
            }

            withClue("collect proper data from flow") {
                runTest {
                    selectedChannelUseCase.loadSelectedChannelsFlow().collect { resultList ->
                        withClue("result is list of model value") {
                            resultList.shouldBeInstanceOf<List<SelectedChannel>>()
                            resultList.first() shouldBeEqualToComparingFields expectedResult.first()
                        }

                        withClue("result elements proper count") {
                            resultList.count() shouldBe 3
                        }

                        withClue("result fields values match expected values first item") {
                            resultList.first().channelId shouldBe expectedResult.first().channelId
                            resultList.first().channelName shouldBe expectedResult.first().channelName
                            resultList.first().parentList shouldBe expectedResult.first().parentList
                        }

                        withClue("result fields values match expected values last item") {
                            resultList.last().channelId shouldBe "testId3"
                            resultList.last().channelName shouldBe "testName3"
                            resultList.last().parentList shouldBe "test"
                        }
                    }
                }
            }
        }

        "add channel item to selected" {
            val availableChannel = AvailableChannel("testId1", "testName1", "iconUrl")
            val channelToAdd =
                SelectedChannelEntity(
                    "testId1",
                    "testName1",
                    order = 4,
                    parentList = "test",
                )

            coEvery {
                selectedChannelRepository.addChannel(channelToAdd)
            } just runs

            withClue("call sequence from selectedChannelRepository execute") {
                runTest {
                    selectedChannelUseCase.addChannelToSelected(availableChannel)

                    coVerifySequence {
                        selectedChannelRepository.loadSelectedChannels("test")
                        selectedChannelRepository.addChannel(channelToAdd)
                    }
                }
            }
        }

        "delete channel item from selected" {
            coEvery {
                selectedChannelRepository.updateChannels(expectedInsertDao)
            } just runs

            coEvery {
                selectedChannelRepository.deleteChannel("testId1")
            } just runs

            withClue("call sequence from selectedChannelRepository execute") {
                runTest {
                    selectedChannelUseCase.deleteChannelFromSelected("testId1")

                    coVerifySequence {
                        selectedChannelRepository.deleteChannel("testId1")
                        selectedChannelRepository.loadSelectedChannels("test")
                        selectedChannelRepository.updateChannels(expectedInsertDao)
                    }
                }
            }
        }

        "update channels order" {
            val notOrdered =
                listOf(
                    SelectedChannel(
                        "testId1",
                        "testName1",
                        "iconUrl",
                        order = 3,
                        parentList = "test",
                    ),
                    SelectedChannel(
                        "testId2",
                        "testName2",
                        "iconUrl",
                        order = 4,
                        parentList = "test",
                    ),
                    SelectedChannel(
                        "testId3",
                        "testName3",
                        "iconUrl",
                        order = 2,
                        parentList = "test",
                    ),
                )

            coEvery {
                selectedChannelRepository.updateChannels(expectedInsertDao)
            } just runs

            withClue("single call from selectedChannelRepository execute") {
                runTest {
                    selectedChannelUseCase.updateChannelsOrdersAfterReorder(notOrdered)

                    coVerify(exactly = 1) {
                        selectedChannelRepository.updateChannels(expectedInsertDao)
                    }

                    confirmVerified(selectedChannelRepository)
                }
            }
        }
    }
})

private fun createPreferenceMockRepository(): PreferenceRepository {
    val preferenceRepository = mockk<PreferenceRepository>()
    coEvery {
        preferenceRepository.targetList
    } returns
        flow {
            emit("test")
        }
    return preferenceRepository
}

private fun createSelectedChannelMockRepository(): SelectedChannelRepository {
    val selectedChannelRepository = mockk<SelectedChannelRepository>()
    coEvery {
        selectedChannelRepository.loadSelectedChannelsFlow("test")
    } returns
        flow {
            emit(expectedResultDao)
        }

    coEvery {
        selectedChannelRepository.loadSelectedChannels("test")
    } returns expectedResultDao

    return selectedChannelRepository
}

private val expectedResultDao
    get() =
        listOf(
            SelectedChannelWithIconEntity(
                channel =
                    SelectedChannelEntity(
                        "testId1",
                        "testName1",
                        order = 1,
                        parentList = "test",
                    ),
                allChannel = AvailableChannelEntity("testId1", "testName1", "testIcon1"),
            ),
            SelectedChannelWithIconEntity(
                channel =
                    SelectedChannelEntity(
                        "testId2",
                        "testName2",
                        order = 2,
                        parentList = "test",
                    ),
                allChannel = AvailableChannelEntity("testId2", "testName2", "testIcon2"),
            ),
            SelectedChannelWithIconEntity(
                channel =
                    SelectedChannelEntity(
                        "testId3",
                        "testName3",
                        order = 3,
                        parentList = "test",
                    ),
                allChannel = AvailableChannelEntity("testId3", "testName3", "testIcon3"),
            ),
        )

private val expectedInsertDao
    get() =
        listOf(
            SelectedChannelEntity("testId1", "testName1", order = 1, parentList = "test"),
            SelectedChannelEntity("testId2", "testName2", order = 2, parentList = "test"),
            SelectedChannelEntity("testId3", "testName3", order = 3, parentList = "test"),
        )
