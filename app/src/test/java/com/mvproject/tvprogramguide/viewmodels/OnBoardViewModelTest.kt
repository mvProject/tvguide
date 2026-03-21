package com.mvproject.tvprogramguide.viewmodels

import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsWithProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelViewModel
import com.mvproject.tvprogramguide.ui.screens.channels.selected.actions.ChannelsViewAction
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardViewModelTest : FunSpec({
    lateinit var channelListRepository: IChannelListRepository
    lateinit var selectedChannelsWithPrograms: GetSelectedChannelsWithProgramsUseCase
    lateinit var preferenceRepository: IPreferenceRepository
    lateinit var toggleProgramSchedule: ToggleProgramScheduleUseCase
    lateinit var selectChannelListUseCase: SelectChannelListUseCase
    lateinit var channelViewModel: ChannelViewModel

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        channelListRepository = mockk<IChannelListRepository>()
        selectedChannelsWithPrograms = mockk<GetSelectedChannelsWithProgramsUseCase>()
        preferenceRepository = mockk<IPreferenceRepository>()
        toggleProgramSchedule = mockk<ToggleProgramScheduleUseCase>()
        selectChannelListUseCase = mockk<SelectChannelListUseCase>()

        every { preferenceRepository.loadOnBoardState() } returns flowOf(false)
        every { channelListRepository.loadChannelsListsAsFlow() } returns flowOf(emptyList())

        channelViewModel = ChannelViewModel(
            channelListRepository = channelListRepository,
            selectedChannelsWithPrograms = selectedChannelsWithPrograms,
            preferenceRepository = preferenceRepository,
            toggleProgramSchedule = toggleProgramSchedule,
            selectChannelListUseCase = selectChannelListUseCase,
        )
    }

    afterTest {
        unmockkAll()
        Dispatchers.resetMain()
    }

    test("initial viewState is default") {
        withClue("viewState has default values on init") {
            channelViewModel.viewState.value.isOnboard shouldBe false
            channelViewModel.viewState.value.isLoading shouldBe false
            channelViewModel.viewState.value.channels shouldBe emptyList()
        }
    }

    test("complete onboard action calls setOnBoardState false") {
        coEvery {
            preferenceRepository.setOnBoardState(false)
        } just runs

        channelViewModel.processAction(ChannelsViewAction.CompleteOnBoard)

        coVerify(exactly = 1) {
            preferenceRepository.setOnBoardState(false)
        }
    }
})
