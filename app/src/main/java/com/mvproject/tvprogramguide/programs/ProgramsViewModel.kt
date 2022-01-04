package com.mvproject.tvprogramguide.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.repository.CustomListRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.utils.Mappers.toSortedSelectedChannelsPrograms
import com.mvproject.tvprogramguide.utils.createInputDataForUri
import com.mvproject.tvprogramguide.workers.UpdateProgramsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProgramsViewModel @Inject constructor(
    private val storeManager: StoreManager,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val customListRepository: CustomListRepository,
    private val workManager: WorkManager
) : ViewModel() {

    val outputWorkInfo: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_PROGRAMS)

    private var _selectedList = MutableStateFlow(storeManager.defaultChannelList)
    val selectedList = _selectedList.asStateFlow()

    private var _selectedPrograms = MutableStateFlow<List<IChannel>>(emptyList())
    val selectedPrograms = _selectedPrograms.asStateFlow()

    private var _availableLists: List<CustomListEntity> = emptyList()

    private var savedList = storeManager.defaultChannelList

    private var visibleCount = storeManager.programByChannelDefaultCount

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.loadChannelsLists().collect {
                _availableLists = it
            }
        }
    }

    fun checkSavedList() {
        savedList = storeManager.defaultChannelList
        viewModelScope.launch(Dispatchers.IO) {
            _selectedList.emit(savedList)
        }
    }

    val availableLists get() = _availableLists.map { it.name }

    fun reloadChannels() {
        updatePrograms()
    }

    fun saveSelectedList(listName: String) {
        storeManager.setDefaultChannelList(listName)
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
                startDownload(false, missingIds.toTypedArray())
            }

        } else {
            Timber.d("testing no current saved list")
        }
    }

    fun checkForUpdates() {
        visibleCount = storeManager.programByChannelDefaultCount
        if (storeManager.isNeedFullProgramsUpdate) {
            startDownload(false)
            Timber.d("testing is shouldFullUpdate")
        } else {
            Timber.d("testing is not shouldFullUpdate")
        }
    }

    private fun startDownload(isNotificationOn: Boolean, missing: Array<String> = emptyArray()) {
        val channelRequest = OneTimeWorkRequest.Builder(UpdateProgramsWorker::class.java)
            .setInputData(createInputDataForUri(savedList, missing, isNotificationOn))
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_PROGRAMS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }
}
