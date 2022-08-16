package com.mvproject.tvprogramguide.ui.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.utils.AppConstants.DEFAULT_DELAY
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.utils.DOWNLOAD_CHANNELS
import com.mvproject.tvprogramguide.domain.utils.createInputDataForUpdate
import com.mvproject.tvprogramguide.domain.workers.UpdateChannelsWorker
import com.mvproject.tvprogramguide.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val currentTheme = preferenceRepository.loadAppSettings()
        .map { settings ->
            AppThemeOptions.getThemeById(settings.appTheme)
        }

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(NO_VALUE_STRING)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            preferenceRepository.isNeedAvailableChannelsUpdate
                .collectLatest { updateRequired ->
                    if (updateRequired) {
                        startChannelsUpdate()
                    }
                }
        }

        viewModelScope.launch {
            preferenceRepository.loadOnBoardState().collect { completed ->
                if (completed) {
                    _startDestination.value = AppRoutes.Channels.route
                } else {
                    _startDestination.value = AppRoutes.OnBoard.route
                }
                delay(DEFAULT_DELAY)
                _isLoading.value = false
            }
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
