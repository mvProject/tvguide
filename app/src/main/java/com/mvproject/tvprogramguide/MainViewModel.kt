package com.mvproject.tvprogramguide

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.helpers.NetworkHelper
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.utils.createInputDataForPartialUpdate
import com.mvproject.tvprogramguide.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.workers.FullUpdateProgramsWorker
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
        Timber.d("testing checkAvailableChannelsUpdate ${storeHelper.isNeedAvailableChannelsUpdate}")
        if (storeHelper.isNeedAvailableChannelsUpdate) {
            startChannelsUpdate()
        }
    }

    fun checkFullProgramsUpdate() {
        Timber.d("testing checkAvailableChannelsUpdate ${storeHelper.isNeedFullProgramsUpdate}")
        if (storeHelper.isNeedFullProgramsUpdate) {
            startProgramsFullUpdate()
        }
    }

    private fun startChannelsUpdate() {
        val channelRequest = OneTimeWorkRequest.Builder(UpdateChannelsWorker::class.java)
            .setInputData(createInputDataForUpdate())
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_CHANNELS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }

    private fun startProgramsFullUpdate() {
        val channelRequest = OneTimeWorkRequest.Builder(FullUpdateProgramsWorker::class.java)
            .setInputData(createInputDataForUpdate())
            .build()
        workManager.enqueueUniqueWork(
            DOWNLOAD_FULL_PROGRAMS,
            ExistingWorkPolicy.REPLACE,
            channelRequest
        )
    }
}