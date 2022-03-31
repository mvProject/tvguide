package com.mvproject.tvprogramguide.ui.main

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.helpers.NetworkHelper
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.utils.DOWNLOAD_FULL_PROGRAMS
import com.mvproject.tvprogramguide.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import com.mvproject.tvprogramguide.domain.workers.UpdateChannelsWorker
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
            Timber.d("testing no connection")
        }
    }

    private fun startProgramsFullUpdate() {
        if (networkHelper.isNetworkConnected()) {
            val programRequest = OneTimeWorkRequest.Builder(FullUpdateProgramsWorker::class.java)
                //.setConstraints(
                //    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                //)
                .setInputData(createInputDataForUpdate())
                .build()
            workManager.enqueueUniqueWork(
                DOWNLOAD_FULL_PROGRAMS,
                ExistingWorkPolicy.REPLACE,
                programRequest
            )
        } else {
            Timber.d("testing no connection")
        }
    }
}
