package com.mvproject.tvprogramguide.usecase

import io.kotest.core.spec.style.StringSpec

class SelectedChannelUseCaseTest :
    StringSpec({

        // TODO refactor tests

        /*lateinit var selectedChannelRepository: SelectedChannelRepository

        beforeTest {
            selectedChannelRepository = createSelectedChannelMockRepository()
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
            }

            "delete channel item from selected" {
                coEvery {
                    selectedChannelRepository.updateChannels(expectedInsertDao)
                } just runs

                coEvery {
                    selectedChannelRepository.deleteChannel("testId1")
                } just runs
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
            }
        }*/
    })

/*
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
*/
