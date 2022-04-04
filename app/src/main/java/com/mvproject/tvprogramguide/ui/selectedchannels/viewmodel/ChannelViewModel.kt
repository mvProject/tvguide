package com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.helpers.NetworkHelper
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.data.entity.CustomListEntity
import com.mvproject.tvprogramguide.data.model.CustomList
import com.mvproject.tvprogramguide.data.model.SelectedChannelModel
import com.mvproject.tvprogramguide.domain.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.domain.repository.CustomListRepository
import com.mvproject.tvprogramguide.domain.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.utils.*
import com.mvproject.tvprogramguide.domain.utils.Mappers.toSortedSelectedChannelsPrograms
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import com.mvproject.tvprogramguide.domain.workers.PartiallyUpdateProgramsWorker
import com.mvproject.tvprogramguide.domain.workers.UpdateChannelsWorker
import com.mvproject.tvprogramguide.utils.Utils.obtainIndexOrZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper
) : ViewModel() {

  // val partiallyUpdateWorkInfo: LiveData<List<WorkInfo>> =
  //     workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

  // val fullUpdateWorkInfo: LiveData<List<WorkInfo>> =
  //     workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_FULL_PROGRAMS)

    private var _selectedList = MutableStateFlow(storeHelper.defaultChannelList)
    val selectedList = _selectedList.asStateFlow()

    private var _selectedPrograms = MutableStateFlow<List<SelectedChannelModel>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var _availableLists: List<CustomList> = emptyList()

    private var savedList = storeHelper.defaultChannelList

    private var visibleCount = storeHelper.programByChannelDefaultCount

    init {
        Timber.i("testing ChannelViewModel init")
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists().collect {
                _availableLists = it
            }
        }
    }

    fun checkSavedList() {
        savedList = storeHelper.defaultChannelList
        viewModelScope.launch(Dispatchers.IO) {
            _selectedList.emit(savedList)
        }
    }

    val availableLists get() = _availableLists.map { it.listName }

    fun reloadChannels() {
        visibleCount = storeHelper.programByChannelDefaultCount
        updatePrograms()
    }

    fun saveSelectedList(listName: String) {
        storeHelper.setDefaultChannelList(listName)
        checkSavedList()
        updatePrograms()
    }

    val obtainListIndex
        get() = availableLists.obtainIndexOrZero(savedList)

    private fun updatePrograms() = viewModelScope.launch(Dispatchers.IO) {
        if (savedList.isNotEmpty()) {
            val selectedChannels =
                selectedChannelRepository.loadSelectedChannels(savedList)

            val selectedChannelIds = selectedChannels.map { it.channelId }
            val programsWithChannels =
                channelProgramRepository.loadPrograms(selectedChannelIds)

            val programs = programsWithChannels
                .toSortedSelectedChannelsPrograms(selectedChannels, visibleCount)
            _selectedPrograms.emit(programs)

            val obtainedChannelsIds = programsWithChannels.groupBy { it.channel }.keys
            val obtainedChannelsIdsCount = channelProgramRepository.loadProgramsCount(selectedChannelIds)
            if (selectedChannelIds.count() > obtainedChannelsIdsCount) {
                val missingIds = selectedChannelIds.minus(obtainedChannelsIds)
                startPartiallyUpdate(missingIds.toTypedArray())
            }
        } else {
            Timber.e("testing no current saved list")
        }
    }

    private fun startPartiallyUpdate(ids: Array<String> = emptyArray()) {
        val channelRequest = OneTimeWorkRequest.Builder(PartiallyUpdateProgramsWorker::class.java)
            .setInputData(createInputDataForPartialUpdate(ids = ids))
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_PROGRAMS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }

    fun checkAvailableChannelsUpdate() {
        if (storeHelper.isNeedAvailableChannelsUpdate) {
            startChannelsUpdate()
        }
    }

    fun checkFullProgramsUpdate() {
        if (storeHelper.isNeedFullProgramsUpdate) {
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
                ExistingWorkPolicy.REPLACE,
                channelRequest
            )
        } else {
            Timber.e("testing no connection")
        }
    }

    private fun startProgramsFullUpdate() {
        val programRequest = OneTimeWorkRequest.Builder(FullUpdateProgramsWorker::class.java)
            //.setConstraints(
            //    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            //)
            .setInputData(createInputDataForUpdate())
            .build()
        if (networkHelper.isNetworkConnected()) {
            workManager.enqueueUniqueWork(
                DOWNLOAD_FULL_PROGRAMS,
                ExistingWorkPolicy.REPLACE,
                programRequest
            )
        } else {
            Timber.e("testing no connection")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("testing ChannelViewModel onCleared")
    }
}
