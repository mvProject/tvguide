package com.mvproject.tvprogramguide.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.usecases.CleanProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.utils.AppConstants
import com.mvproject.tvprogramguide.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.utils.buildFullUpdateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
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
    preferenceRepository: PreferenceRepository,
    private val updateChannelsInfoUseCase: UpdateChannelsInfoUseCase,
    private val cleanProgramsUseCase: CleanProgramsUseCase,

    ) : ViewModel() {
    private val fullUpdateWorkInfoFlow =
        workManager.getWorkInfosForUniqueWorkFlow(DOWNLOAD_PROGRAMS)

    val currentTheme =
        preferenceRepository
            .loadAppSettings()
            .map { settings ->
                AppThemeOptions.getThemeById(settings.appTheme)
            }

    private var isUpdating = false

    init {
        fullUpdateWorkInfoFlow
            .onEach { state ->
                if (state.isNullOrEmpty()) {
                    Timber.w("worker updateWorkInfo null")
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
        //    preferenceRepository.getChannelsForUpdate(),
            preferenceRepository.getProgramsUpdateRequiredState()
        ) { channelsUpdateRequired, plannedUpdateRequired, manualUpdateRequired ->

            if (channelsUpdateRequired) {
                updateChannelsInfoUseCase()
            }

            if (plannedUpdateRequired || manualUpdateRequired) {
                startProgramsUpdate(requestForUpdate = buildFullUpdateRequest())
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            cleanProgramsUseCase()
        }
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
