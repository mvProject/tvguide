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
import com.mvproject.tvprogramguide.utils.COUNT_ZERO
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

    private var _selected = MutableStateFlow(storeManager.defaultChannelList)
    val selected = _selected.asStateFlow()

    private var _channels = MutableStateFlow<List<IChannel>>(emptyList())
    val channels = _channels.asStateFlow()

    private var _availableLists: List<CustomListEntity> = emptyList()

    private var savedList = storeManager.defaultChannelList

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
            _selected.emit(savedList)
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
            val alreadySelected =
                selectedChannelRepository.loadSelectedChannels(savedList)

            val alreadySelectedIds = alreadySelected.map { it.channelId }
            val channels =
                channelProgramRepository.load(alreadySelectedIds)

            val alreadyObtainedChannels = channels.groupBy { it.channel }.keys
            if (channels.count() > COUNT_ZERO) {
                val programs = channels
                    .filter { it.dateTime + it.duration > System.currentTimeMillis() }
                    .toSortedSelectedChannelsPrograms(alreadySelected, 4)

                _channels.emit(programs)
            } else {
                 startDownload(false)
            }

            if (alreadySelectedIds.count() > alreadyObtainedChannels.count()) {
                val missingIds = alreadySelectedIds.minus(alreadyObtainedChannels)
                startDownload(false, missingIds.toTypedArray())
            }

        } else {
            Timber.d("testing no current saved list")
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
