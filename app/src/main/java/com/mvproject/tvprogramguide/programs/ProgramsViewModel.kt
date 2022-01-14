package com.mvproject.tvprogramguide.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.helpers.NetworkHelper
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.repository.CustomListRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.utils.Mappers.toSortedSelectedChannelsPrograms
import com.mvproject.tvprogramguide.utils.createInputDataForPartialUpdate
import com.mvproject.tvprogramguide.workers.PartiallyUpdateProgramsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProgramsViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val partiallyUpdateWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

    val fullUpdateWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_FULL_PROGRAMS)

    private var _selectedList = MutableStateFlow(storeHelper.defaultChannelList)
    val selectedList = _selectedList.asStateFlow()

    private var _selectedPrograms = MutableStateFlow<List<IChannel>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var _availableLists: List<CustomListEntity> = emptyList()

    private var savedList = storeHelper.defaultChannelList

    private var visibleCount = storeHelper.programByChannelDefaultCount

    init {
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

    val availableLists get() = _availableLists.map { it.name }

    fun reloadChannels() {
        updatePrograms()
       // if (networkHelper.isNetworkConnected()){
       //     Timber.d("testing network connected")
       // } else {
       //     Timber.d("testing network not connected")
       // }
    }

    fun saveSelectedList(listName: String) {
        storeHelper.setDefaultChannelList(listName)
        checkSavedList()
        updatePrograms()
    }

    private fun updatePrograms() = viewModelScope.launch(Dispatchers.IO) {
        if (savedList.isNotEmpty()) {
            val selectedChannels =
                selectedChannelRepository.loadSelectedChannels(savedList)

            val selectedChannelIds = selectedChannels.map { it.channelId }

            val programsWithChannels =
                channelProgramRepository.loadPrograms(selectedChannelIds)

            val obtainedChannelsIds = programsWithChannels.groupBy { it.channel }.keys

            val programs = programsWithChannels
                .toSortedSelectedChannelsPrograms(selectedChannels, visibleCount)

            _selectedPrograms.emit(programs)

            if (selectedChannelIds.count() > obtainedChannelsIds.count()) {
                val missingIds = selectedChannelIds.minus(obtainedChannelsIds)
                startPartiallyUpdate(missingIds.toTypedArray())
            }

        } else {
            Timber.d("testing no current saved list")
        }
    }

    fun checkForUpdates() {
        visibleCount = storeHelper.programByChannelDefaultCount
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
}
