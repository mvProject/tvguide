package com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.obtainIndexOrZero
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import com.mvproject.tvprogramguide.domain.usecases.SortedProgramsUseCase
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.domain.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import com.mvproject.tvprogramguide.domain.workers.UpdateChannelsWorker
import com.mvproject.tvprogramguide.ui.selectedchannels.ChannelsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    private val sortedProgramsUseCase: SortedProgramsUseCase
) : ViewModel() {

    // val partiallyUpdateWorkInfo: LiveData<List<WorkInfo>> =
    //     workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

    val fullUpdateWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_FULL_PROGRAMS)

    private var _selectedPrograms = MutableStateFlow(ChannelsViewState())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var _availableLists: List<UserChannelsList> = emptyList()

    private val currentChannelList
        get() = storeHelper.defaultChannelList

    private val channelList = MutableStateFlow(currentChannelList)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists().collect { lists ->
                _availableLists = lists
            }
        }

        channelList.mapLatest { listName ->
            _selectedPrograms.value =
                selectedPrograms.value.copy(listName = listName)
            updatePrograms()
        }.launchIn(viewModelScope)
    }

    fun reloadChannels() {
        val current = currentChannelList
        if (channelList.value != current) {
            channelList.value = current
        }
        updatePrograms()
    }

    fun applyList(listName: String) {
        storeHelper.setDefaultChannelList(name = listName)
        channelList.value = listName
    }

    val availableLists
        get() = _availableLists.map { channels ->
            channels.listName
        }
    val obtainListIndex
        get() = availableLists.obtainIndexOrZero(target = channelList.value)

    private fun updatePrograms() {
        if (channelList.value.isEmpty()) {
            Timber.e("testing no current saved list")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val programs = sortedProgramsUseCase
                    .retrieveSelectedChannelWithPrograms()

                _selectedPrograms.value =
                    selectedPrograms.value.copy(
                        listName = channelList.value,
                        programs = programs.sortedBy { it.selectedChannel.order }
                    )

                //  val obtainedChannelsIds = programs.map { it.channel.channelId }

                val selectedChannelIds = selectedChannelRepository
                    .loadSelectedChannels(listName = channelList.value)
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

    fun checkAvailableChannelsUpdate() {
        if (storeHelper.isNeedAvailableChannelsUpdate) {
            startChannelsUpdate()
        }
    }

    fun checkFullProgramsUpdate() {
        if (channelList.value.isNotEmpty() && storeHelper.isNeedFullProgramsUpdate) {
            startProgramsFullUpdate()
        }
    }

    private fun startChannelsUpdate() {
        if (networkHelper.isNetworkConnected()) {
            val channelRequest = OneTimeWorkRequest.Builder(UpdateChannelsWorker::class.java)
                .setInputData(createInputDataForUpdate())
                .build()
            workManager.enqueueUniqueWork(
                DOWNLOAD_CHANNELS,
                ExistingWorkPolicy.KEEP,
                channelRequest
            )
        } else {
            Timber.e("testing no connection")
        }
    }

    private fun startProgramsFullUpdate() {
        if (networkHelper.isNetworkConnected()) {
            val programRequest = OneTimeWorkRequest.Builder(FullUpdateProgramsWorker::class.java)
                .setInputData(createInputDataForUpdate())
                .build()
            workManager.enqueueUniqueWork(
                DOWNLOAD_FULL_PROGRAMS,
                ExistingWorkPolicy.KEEP,
                programRequest
            )
        } else {
            Timber.e("testing no connection")
        }
    }
}
