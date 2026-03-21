package com.mvproject.tvprogramguide.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.usecases.CleanProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel(
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper,
    preferenceRepository: IPreferenceRepository,
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
                }
            }.launchIn(viewModelScope)


        combine(
            preferenceRepository.isNeedAvailableChannelsUpdate,
            preferenceRepository.isNeedFullProgramsUpdate,
            preferenceRepository.getProgramsUpdateRequiredState()
        ) { channelsUpdateRequired, plannedUpdateRequired, manualUpdateRequired ->

            if (channelsUpdateRequired) {
                withContext(Dispatchers.IO) { updateChannelsInfoUseCase() }
            }

            if (plannedUpdateRequired || manualUpdateRequired) {
                startProgramsUpdate()
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            cleanProgramsUseCase()
        }
    }

    private fun startProgramsUpdate() {
        if (networkHelper.isNetworkConnected() && !isUpdating) {
            val requestForUpdate = OneTimeWorkRequest
                .Builder(FullUpdateProgramsWorker::class.java)
                .build()


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

    private companion object {
        const val DOWNLOAD_PROGRAMS = "DOWNLOAD_PROGRAMS"
    }
}
