package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.mappers.Mappers.toSingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import com.mvproject.tvprogramguide.utils.parseChannelName
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

/**
 * Use case to retrieving and updating sorted programs
 * @property selectedChannelRepository the selectedChannelRepository repository
 * @property channelProgramRepository the channelProgramRepository repository
 * @property preferenceRepository the preferences repository
 * @property programSchedulerHelper the scheduler helper
 */
class SortedProgramsUseCase
    @Inject
    constructor(
        private val selectedChannelRepository: SelectedChannelRepository,
        private val channelProgramRepository: ChannelProgramRepository,
        private val preferenceRepository: PreferenceRepository,
        private val programSchedulerHelper: ProgramSchedulerHelper,
    ) {
        /**
         * Obtain list of programs for selected channel and sorted by date
         *
         * @param channelId the alarm id
         * @return sorted list of programs
         */
        suspend fun retrieveProgramsForChannel(channelId: String) =
            channelProgramRepository
                .loadProgramsForChannel(channelId = channelId)
                .toSingleChannelWithPrograms()

        /**
         * Update selected channel for mark as scheduled or cancel (if already was scheduled)
         *
         * @param programSchedule data object with channel info
         */
        suspend fun updateProgramScheduleWithAlarm(programSchedule: ProgramSchedule) {
            val scheduleProgram =
                channelProgramRepository
                    .loadProgramsForChannel(channelId = programSchedule.channelId)
                    .firstOrNull { program ->
                        program.title == programSchedule.programTitle
                    }

            scheduleProgram?.let { selected ->
                val channelName =
                    selectedChannelRepository
                        .loadChannelNameById(selectedId = programSchedule.channelId)
                        .parseChannelName()

                val program =
                    if (selected.scheduledId == null) {
                        val id = Random.nextLong()
                        programSchedulerHelper.scheduleProgramAlarm(
                            programSchedule =
                                programSchedule.copy(
                                    scheduleId = id,
                                    channelTitle = channelName,
                                    triggerTime = selected.dateTimeStart,
                                ),
                        )
                        selected.copy(scheduledId = id)
                    } else {
                        // cancel alert
                        val idForCancel = selected.scheduledId
                        programSchedulerHelper.cancelProgramAlarm(schedulerId = idForCancel)
                        selected.copy(scheduledId = null)
                    }
                channelProgramRepository.updateProgram(program = program)
            } ?: Timber.e("scheduleProgram is null")
        }

        /**
         * Check if update needed for channels in current user list
         * when new channel add for example
         *
         * @param obtainedChannelsIds id list of current channels with loaded programs
         * @return id list of channels for update or null if no updates
         */
        suspend fun checkProgramsUpdateRequired(obtainedChannelsIds: List<String>): Array<String>? {
            val currentChannelList = preferenceRepository.loadDefaultUserList().first()

            val selectedChannelIds =
                selectedChannelRepository
                    .loadSelectedChannels(listName = currentChannelList)
                    .map { entity ->
                        entity.channel.channelId
                    }

            val obtainedChannelsIdsCount =
                channelProgramRepository
                    .loadProgramsCount(channelsIds = selectedChannelIds)

            if (selectedChannelIds.count() > obtainedChannelsIdsCount) {
                return selectedChannelIds.minus(obtainedChannelsIds.toSet()).toTypedArray()
            }
            return null
        }
    }
