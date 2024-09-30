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
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.utils.AppConstants
import com.mvproject.tvprogramguide.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.utils.DOWNLOAD_PROGRAMS
import com.mvproject.tvprogramguide.utils.TimeUtils.tzCurrent
import com.mvproject.tvprogramguide.utils.TimeUtils.tzSource
import com.mvproject.tvprogramguide.utils.TimeUtils.tzSource1
import com.mvproject.tvprogramguide.utils.buildFullUpdateRequest
import com.mvproject.tvprogramguide.utils.convertTimeToReadableFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

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
            preferenceRepository
                .loadAppSettings()
                .map { settings ->
                    AppThemeOptions.getThemeById(settings.appTheme)
                }

        private var isUpdating = false

        init {

            val test = "14/09/2024 18:27-20:19 (112)"

            val (start, end) = parseDateTime(test)

            Timber.d("testing parseDateTime start ${start.convertTimeToReadableFormat()}")
            Timber.d("testing parseDateTime end ${end.convertTimeToReadableFormat()}")

            val startInstant = Instant.fromEpochMilliseconds(start)
            val dateTimeStart =
                startInstant
                    .toLocalDateTime(tzCurrent)
                    .toInstant(tzSource)
                    .toEpochMilliseconds()

            val endInstant = Instant.fromEpochMilliseconds(end)
            val dateTimeEnd =
                endInstant
                    .toLocalDateTime(tzCurrent)
                    .toInstant(tzSource)
                    .toEpochMilliseconds()

            Timber.d("testing parseDateTime dateTimeStart ${dateTimeStart.convertTimeToReadableFormat()}")
            Timber.d("testing parseDateTime dateTimeEnd ${dateTimeEnd.convertTimeToReadableFormat()}")

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
                            Timber.i("fullUpdateWorkInfoFlow worker channel update $current/$count")
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

fun parseDateTime(input: String): Pair<Long, Long> {
    // Split the input string
    val (dateTimeStart, durationPart) = input.split(" (")
    val (date, time) = dateTimeStart.split(" ")
    val (start, end) = time.split("-")
    val duration = durationPart.removeSuffix(")").toDouble().toInt()
    Timber.d("testing parseDateTime duration $duration")
    val localDate = dateFormat.parse(date)
    val startLocalTime = timeFormat.parse(start)

/*    val startTime = "$date $start"

    Timber.d("testing parseDateTime startTime $startTime")*/
    val startDateTime =
        LocalDateTime(
            localDate,
            startLocalTime,
        )

/*    val startDateTime = startTime.toMillisSlashed()
    val entDateTime = startDateTime + duration.minutes.inWholeMilliseconds*/

    val startInstant = startDateTime.toInstant(tzSource1)
    val endInstant = startInstant.plus(duration.minutes)

    return Pair(startInstant.toEpochMilliseconds(), endInstant.toEpochMilliseconds())
    // return Pair(startDateTime, entDateTime)
}

val dateFormat =
    LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }

val timeFormat =
    LocalTime.Format {
        hour()
        char(':')
        minute()
    }
