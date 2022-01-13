package com.mvproject.tvprogramguide

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.helpers.NetworkHelper
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.workers.UpdateAllProgramsWorker
import com.mvproject.tvprogramguide.workers.UpdateChannelsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    fun checkAvailableChannelsUpdate() {
        Timber.d("testing MainViewModel checkAvailableChannelsUpdate is ${storeHelper.isNeedAvailableChannelsUpdate}")
        if (storeHelper.isNeedAvailableChannelsUpdate) {
            startChannelsDownload()
        }
    }

    fun checkFullProgramsUpdate() {
        Timber.d("testing MainViewModel checkFullProgramsUpdate is ${storeHelper.isNeedFullProgramsUpdate}")
        if (storeHelper.isNeedFullProgramsUpdate) {
            //startProgramsFullDownload()
        }
    }

    private fun startChannelsDownload() {
        val channelRequest = OneTimeWorkRequest.Builder(UpdateChannelsWorker::class.java)
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_CHANNELS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }

    private fun startProgramsFullDownload(
        isNotificationOn: Boolean,
        missing: Array<String> = emptyArray()
    ) {
        val channelRequest = OneTimeWorkRequest.Builder(UpdateAllProgramsWorker::class.java)
            //  .setInputData(createInputDataForUri(savedList, missing, isNotificationOn))
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_FULL_PROGRAMS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }
}
