package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.data.repository.ChannelProgramRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.data.utils.Mappers.asSelectedChannelsFromEntities
import com.mvproject.tvprogramguide.data.utils.Mappers.toSelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.Mappers.toSingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.parseChannelName
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class SortedProgramsUseCase @Inject constructor(
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelProgramRepository: ChannelProgramRepository,
    private val preferenceRepository: PreferenceRepository,
    private val programSchedulerHelper: ProgramSchedulerHelper,
) {

    private val currentChannelList
        get() = runBlocking { preferenceRepository.loadDefaultUserList().first() }

    suspend fun retrieveSelectedChannelWithPrograms(): List<SelectedChannelWithPrograms> {
        val currentChannelList = preferenceRepository.loadDefaultUserList().first()

        val selectedChannels =
            selectedChannelRepository
                .loadSelectedChannels(listName = currentChannelList)
                .asSelectedChannelsFromEntities()

        val selectedChannelIds = selectedChannels.map { channel ->
            channel.channelId
        }

        val programsWithChannels =
            channelProgramRepository.loadProgramsForChannels(channelsIds = selectedChannelIds)

        return programsWithChannels
            .toSelectedChannelWithPrograms(
                alreadySelected = selectedChannels,
                itemsCount = preferenceRepository
                    .loadAppSettings()
                    .first()
                    .programsViewCount
            )
    }

    suspend fun retrieveProgramsForChannel(channelId: String) =
        channelProgramRepository
            .loadProgramsForChannel(channelId = channelId)
            .toSingleChannelWithPrograms()


    suspend fun updateProgramScheduleWithAlarm(programSchedule: ProgramSchedule) {
        val scheduleProgram = channelProgramRepository
            .loadProgramsForChannel(channelId = programSchedule.channelId)
            .firstOrNull { program ->
                program.title == programSchedule.programTitle
            }

        scheduleProgram?.let { selected ->
            val channelName = selectedChannelRepository
                .loadChannelNameById(selectedId = programSchedule.channelId)
                .parseChannelName()

            val program = if (selected.scheduledId == null) {
                val id = Random.nextLong()
                programSchedulerHelper.scheduleProgramAlarm(
                    programSchedule = programSchedule.copy(
                        scheduleId = id,
                        channelTitle = channelName,
                        triggerTime = selected.dateTimeStart
                    )
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
}
