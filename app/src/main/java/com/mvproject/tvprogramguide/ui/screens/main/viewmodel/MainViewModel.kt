package com.mvproject.tvprogramguide.ui.screens.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.utils.AppConstants
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_DELAY
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.utils.buildFullUpdateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val workManager: WorkManager,
        private val networkHelper: NetworkHelper,
        private val preferenceRepository: PreferenceRepository,
        private val updateChannelsInfoUseCase: UpdateChannelsInfoUseCase,
    ) : ViewModel() {
        private val fullUpdateWorkInfoFlow =
            workManager.getWorkInfosForUniqueWorkFlow(DOWNLOAD_PROGRAMS)

        val currentTheme =
            preferenceRepository.loadAppSettings()
                .map { settings ->
                    AppThemeOptions.getThemeById(settings.appTheme)
                }

        private val _startDestination: MutableState<String> = mutableStateOf(NO_VALUE_STRING)
        val startDestination: State<String> = _startDestination

        private var isUpdating = false

        init {

            viewModelScope.launch {
                preferenceRepository.loadOnBoardState().collect { completed ->
                    if (completed) {
                        _startDestination.value = AppRoutes.Channels.route
                    } else {
                        _startDestination.value = AppRoutes.OnBoard.route
                    }
                    delay(DEFAULT_DELAY)
                }
            }

            fullUpdateWorkInfoFlow.onEach { state ->
                if (state.isNullOrEmpty()) {
                    Timber.w("testing worker updateWorkInfo null")
                } else {
                    val workInfo = state.first()
                    setUpdatingState(workInfo.state != WorkInfo.State.SUCCEEDED)
                    if (workInfo.state == WorkInfo.State.RUNNING) {
                        val progress = workInfo.progress
                        val current = progress.getInt(CHANNEL_INDEX, AppConstants.COUNT_ZERO)
                        val count = progress.getInt(CHANNEL_COUNT, AppConstants.COUNT_ZERO)
                        Timber.i("testing fullUpdateWorkInfoFlow worker channel update $current/$count")
                    }
                }
            }.launchIn(viewModelScope)

            combine(
                preferenceRepository.isNeedAvailableChannelsUpdate,
                preferenceRepository.isNeedFullProgramsUpdate,
                preferenceRepository.loadChannelsUpdateRequired(),
            ) { channelsUpdateRequired, plannedUpdateRequired, manualUpdateRequired ->

                if (channelsUpdateRequired) {
                    updateChannelsInfoUseCase()
                }

                if (plannedUpdateRequired || manualUpdateRequired) {
                    startProgramsUpdate(requestForUpdate = buildFullUpdateRequest())
                }
            }.launchIn(viewModelScope)
        }

        private fun startProgramsUpdate(requestForUpdate: OneTimeWorkRequest) {
            if (networkHelper.isNetworkConnected() && !isUpdating) {
                workManager.enqueueUniqueWork(
                    DOWNLOAD_PROGRAMS,
                    ExistingWorkPolicy.KEEP,
                    requestForUpdate,
                )

                setUpdatingState(true)
            } else {
                Timber.e("startProgramsUpdate no connection")
            }
        }

        private fun setUpdatingState(state: Boolean) {
            isUpdating = state
        }
    }
