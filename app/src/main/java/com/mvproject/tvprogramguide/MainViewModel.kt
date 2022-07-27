package com.mvproject.tvprogramguide

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.domain.PreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.domain.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.domain.workers.UpdateChannelsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storeHelper: StoreHelper,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val currentTheme = preferenceRepository.loadAppTheme()

    init {
        if (storeHelper.isNeedAvailableChannelsUpdate) {
            startChannelsUpdate()
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

    override fun onCleared() {
        super.onCleared()
        Timber.i("testing MainViewModel onCleared")
    }
}
