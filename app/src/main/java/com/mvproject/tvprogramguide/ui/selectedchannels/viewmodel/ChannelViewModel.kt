package com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.utils.obtainIndexOrZero
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.domain.utils.buildFullUpdateRequest
import com.mvproject.tvprogramguide.domain.utils.buildPartiallyUpdateRequest
import com.mvproject.tvprogramguide.ui.selectedchannels.ChannelsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    private val sortedProgramsUseCase: SortedProgramsUseCase,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val fullUpdateWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

    private var _selectedPrograms = MutableStateFlow(ChannelsViewState())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var isOnlineUpdating = false
    private var _availableLists: List<UserChannelsList> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists().collect { lists ->
                _availableLists = lists
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.loadAppSettings().collect {
                updatePrograms()
            }
        }

        preferenceRepository.loadDefaultUserList()
            .mapLatest { listName ->
                _selectedPrograms.value =
                    selectedPrograms.value.copy(listName = listName)
                checkForPartiallyUpdate()
                updatePrograms()
            }
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.isNeedFullProgramsUpdate.collectLatest { updateRequired ->
                if (updateRequired) {
                    startProgramsUpdate(
                        requestForUpdate = buildFullUpdateRequest()
                    )
                }
            }
        }
    }

    val availableLists
        get() = _availableLists.map { channels -> channels.listName }

    val obtainSelectedListIndex
        get() = availableLists
            .obtainIndexOrZero(target = selectedPrograms.value.listName)

    fun reloadProgramsAfterUpdate() {
        if (isOnlineUpdating) {
            updatePrograms()
            isOnlineUpdating = false
        }
    }

    fun applyList(listName: String) {
        _selectedPrograms.value =
            selectedPrograms.value.copy(listName = listName)
        viewModelScope.launch {
            preferenceRepository.setDefaultUserList(listName = listName)
        }
    }

    fun checkForPartiallyUpdate() {
        if (!isOnlineUpdating) {
            viewModelScope.launch(Dispatchers.IO) {
                sortedProgramsUseCase.checkProgramsUpdateRequired(
                    obtainedChannelsIds = selectedPrograms.value.programs
                        .map { item -> item.selectedChannel.channelId }
                )?.let { updateIds ->
                    startProgramsUpdate(
                        requestForUpdate = buildPartiallyUpdateRequest(ids = updateIds)
                    )
                }
            }
        }
    }

    fun toggleProgramSchedule(programForSchedule: ProgramSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            sortedProgramsUseCase
                .updateProgramScheduleWithAlarm(programSchedule = programForSchedule)
            updatePrograms()
        }
    }

    private fun updatePrograms() {
        if (selectedPrograms.value.listName.isEmpty()) {
            Timber.e("no current saved list")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val programs = sortedProgramsUseCase
                    .retrieveSelectedChannelWithPrograms()

                _selectedPrograms.value =
                    selectedPrograms.value.copy(
                        listName = selectedPrograms.value.listName,
                        programs = programs.sortedBy { item ->
                            item.selectedChannel.order
                        }
                    )
            }
        }
    }

    private fun startProgramsUpdate(requestForUpdate: OneTimeWorkRequest) {
        if (networkHelper.isNetworkConnected()) {
            isOnlineUpdating = true
            workManager.enqueueUniqueWork(
                DOWNLOAD_PROGRAMS,
                ExistingWorkPolicy.KEEP,
                requestForUpdate
            )
        } else {
            Timber.e("startProgramsUpdate no connection")
        }
    }
}
