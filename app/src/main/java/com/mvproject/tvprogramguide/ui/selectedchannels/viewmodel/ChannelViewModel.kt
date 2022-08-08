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
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.obtainIndexOrZero
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.domain.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
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
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    private val sortedProgramsUseCase: SortedProgramsUseCase,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    // val partiallyUpdateWorkInfo: LiveData<List<WorkInfo>> =
    //     workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

    val fullUpdateWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_FULL_PROGRAMS)

    private var _selectedPrograms = MutableStateFlow(ChannelsViewState())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var _availableLists: List<UserChannelsList> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists().collect { lists ->
                _availableLists = lists
            }
        }

        preferenceRepository.loadDefaultUserList()
            .mapLatest { listName ->
                _selectedPrograms.value =
                    selectedPrograms.value.copy(listName = listName)
                updatePrograms()
            }
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.isNeedFullProgramsUpdate.collectLatest { updateRequired ->
                if (updateRequired) {
                    startProgramsFullUpdate()
                }
            }
        }
    }

    fun reloadChannels() {
        updatePrograms()
    }

    fun applyList(listName: String) {
        _selectedPrograms.value =
            selectedPrograms.value.copy(listName = listName)
        viewModelScope.launch {
            preferenceRepository.setDefaultUserList(listName = listName)
        }
    }

    val availableLists
        get() = _availableLists
            .map { channels -> channels.listName }

    val obtainListIndex
        get() = availableLists
            .obtainIndexOrZero(target = selectedPrograms.value.listName)

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

                //  val obtainedChannelsIds = programs.map { it.selectedChannel.channelId }

                val selectedChannelIds = selectedChannelRepository
                    .loadSelectedChannels(listName = selectedPrograms.value.listName)
                    .map { entity ->
                        entity.channelId
                    }

                val obtainedChannelsIdsCount =
                    channelProgramRepository
                        .loadProgramsCount(channelsIds = selectedChannelIds)

                if (selectedChannelIds.count() > obtainedChannelsIdsCount) {
                    // val missingIds = selectedChannelIds.minus(obtainedChannelsIds)
                    //startPartiallyUpdate(missingIds.toTypedArray())
                    startProgramsFullUpdate()
                }
            }
        }
    }

    //private fun startPartiallyUpdate(ids: Array<String> = emptyArray()) {
    //    val channelRequest = OneTimeWorkRequest.Builder(PartiallyUpdateProgramsWorker::class.java)
    //        .setInputData(createInputDataForPartialUpdate(ids = ids))
    //        .build()
    //    workManager.enqueueUniqueWork(
    //        DOWNLOAD_PROGRAMS,
    //        ExistingWorkPolicy.REPLACE,
    //        channelRequest
    //    )
    //}

    fun toggleProgramSchedule(programForSchedule: ProgramSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            sortedProgramsUseCase
                .updateProgramScheduleWithAlarm(programSchedule = programForSchedule)
            updatePrograms()
        }
    }

    private fun startProgramsFullUpdate() {
        if (networkHelper.isNetworkConnected()) {
            val programRequest = OneTimeWorkRequest
                .Builder(FullUpdateProgramsWorker::class.java)
                .setInputData(createInputDataForUpdate())
                .build()
            workManager.enqueueUniqueWork(
                DOWNLOAD_FULL_PROGRAMS,
                ExistingWorkPolicy.KEEP,
                programRequest
            )
        } else {
            Timber.e("startProgramsFullUpdate no connection")
        }
    }
}
