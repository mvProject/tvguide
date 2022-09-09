package com.mvproject.tvprogramguide.usecase

import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelUseCase
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SelectedChannelUseCaseTest : StringSpec({

    assertSoftly {
        "load selected channels with flow" {
            val selectedChannelRepository = mockk<SelectedChannelRepository>()
            val preferenceRepository = mockk<PreferenceRepository>()

            val expectedResult = listOf(
                SelectedChannel("testId1", "testName1", "iconUrl", order = 1, parentList = "test"),
                SelectedChannel("testId2", "testName2", "iconUrl", order = 2, parentList = "test"),
                SelectedChannel("testId3", "testName3", "iconUrl", order = 3, parentList = "test"),
            )
            coEvery {
                preferenceRepository.targetList
            } returns flow {
                emit("test")
            }

            coEvery {
                selectedChannelRepository.loadSelectedChannelsFlow("test")
            } returns flow {
                emit(expectedResultDao)
            }

            val selectedChannelUseCase = SelectedChannelUseCase(
                selectedChannelRepository, preferenceRepository
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
            val selectedChannelRepository = mockk<SelectedChannelRepository>()
            val preferenceRepository = mockk<PreferenceRepository>()

            val availableChannel = AvailableChannel("testId1", "testName1", "iconUrl")
            val channelToAdd = SelectedChannelEntity(
                "testId1",
                "testName1",
                "iconUrl",
                order = 4,
                parentList = "test"
            )

            coEvery {
                preferenceRepository.targetList
            } returns flow {
                emit("test")
            }

            coEvery {
                selectedChannelRepository.loadSelectedChannels("test")
            } returns expectedResultDao

            coEvery {
                selectedChannelRepository.addChannel(channelToAdd)
            } just runs

            val selectedChannelUseCase = SelectedChannelUseCase(
                selectedChannelRepository, preferenceRepository
            )

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
            val selectedChannelRepository = mockk<SelectedChannelRepository>()
            val preferenceRepository = mockk<PreferenceRepository>()

            coEvery {
                preferenceRepository.targetList
            } returns flow {
                emit("test")
            }
            coEvery {
                selectedChannelRepository.loadSelectedChannels("test")
            } returns expectedResultDao

            coEvery {
                selectedChannelRepository.updateChannels(expectedResultDao)
            } just runs

            coEvery {
                selectedChannelRepository.deleteChannel("testId1")
            } just runs

            val selectedChannelUseCase = SelectedChannelUseCase(
                selectedChannelRepository, preferenceRepository
            )

            withClue("call sequence from selectedChannelRepository execute") {
                runTest {
                    selectedChannelUseCase.deleteChannelFromSelected("testId1")

                    coVerifySequence {
                        selectedChannelRepository.deleteChannel("testId1")
                        selectedChannelRepository.loadSelectedChannels("test")
                        selectedChannelRepository.updateChannels(expectedResultDao)
                    }
                }
            }
        }

        "update channels order" {
            val selectedChannelRepository = mockk<SelectedChannelRepository>()
            val preferenceRepository = mockk<PreferenceRepository>()

            val notOrdered = listOf(
                SelectedChannel("testId1", "testName1", "iconUrl", order = 3, parentList = "test"),
                SelectedChannel("testId2", "testName2", "iconUrl", order = 4, parentList = "test"),
                SelectedChannel("testId3", "testName3", "iconUrl", order = 2, parentList = "test")
            )

            coEvery {
                preferenceRepository.targetList
            } returns flow {
                emit("test")
            }

            coEvery {
                selectedChannelRepository.updateChannels(expectedResultDao)
            } just runs

            val selectedChannelUseCase = SelectedChannelUseCase(
                selectedChannelRepository, preferenceRepository
            )

            withClue("single call from selectedChannelRepository execute") {
                runTest {
                    selectedChannelUseCase.updateChannelsOrdersAfterReorder(notOrdered)

                    coVerify(exactly = 1) {
                        selectedChannelRepository.updateChannels(expectedResultDao)
                    }

                    confirmVerified(selectedChannelRepository)
                }
            }
        }
    }
})

private val expectedResultDao
    get() = listOf(
        SelectedChannelEntity("testId1", "testName1", "iconUrl", order = 1, parentList = "test"),
        SelectedChannelEntity("testId2", "testName2", "iconUrl", order = 2, parentList = "test"),
        SelectedChannelEntity("testId3", "testName3", "iconUrl", order = 3, parentList = "test"),
    )