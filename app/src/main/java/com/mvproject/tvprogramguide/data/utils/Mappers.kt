package com.mvproject.tvprogramguide.data.utils

import com.mvproject.tvprogramguide.data.model.domain.*
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.UserChannelsListEntity
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramResponse
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO_LONG
import com.mvproject.tvprogramguide.domain.utils.Utils

object Mappers {
    fun List<Program>.toSingleChannelWithPrograms(): List<SingleChannelWithPrograms> {
        val sortedPrograms = mutableListOf<SingleChannelWithPrograms>()
        val programs = this.groupBy { program ->
            program.dateTimeStart.convertDateToReadableFormat()
        }

        programs.forEach { (date, list) ->
            sortedPrograms.add(
                SingleChannelWithPrograms(
                    date = date,
                    programs = list
                )
            )
        }
        return sortedPrograms
    }

    fun List<Program>.toSelectedChannelWithPrograms(
        alreadySelected: List<SelectedChannel>,
        itemsCount: Int = COUNT_ZERO
    ): List<SelectedChannelWithPrograms> {
        val sortedPrograms = mutableListOf<SelectedChannelWithPrograms>()
        val programs = this.groupBy { program ->
            program.channel
        }

        alreadySelected.forEach { chn ->
            programs[chn.channelId]?.let { prg ->
                if (prg.count() > COUNT_ZERO) {
                    sortedPrograms.add(
                        SelectedChannelWithPrograms(
                            selectedChannel = chn,
                            programs = if (itemsCount > COUNT_ZERO)
                                prg.take(itemsCount)
                            else prg
                        )
                    )
                }
            }
        }
        return sortedPrograms
    }

    private fun ProgramResponse.asProgramEntity(
        channelId: String,
        endTime: Long = COUNT_ZERO_LONG
    ) =
        with(this) {
            ProgramEntity(
                dateTimeStart = start.toMillis(),
                dateTimeEnd = endTime,
                title = title,
                description = description,
                category = category,
                channelId = channelId
            )
        }

    fun List<ProgramResponse>.asProgramEntities(
        channelId: String
    ): List<ProgramEntity> {
        val actual = this.filter { programResponse ->
            programResponse.start.toMillis() > Utils.actualDay
        }
        val filtered = if (actual.count() > COUNT_ZERO) actual else this
        val endings = filtered.calculateEndings()

        return filtered.mapIndexed { index, item ->
            val endingTime = endings.elementAtOrNull(index) ?: COUNT_ZERO_LONG
            item.asProgramEntity(
                channelId = channelId,
                endTime = endingTime
            )
        }
    }

    private fun List<ProgramResponse>.calculateEndings(): List<Long> = with(this) {
        val endings = mutableListOf<Long>()
        zipWithNext().forEach { programResponse ->
            endings.add(programResponse.second.start.toMillis())
        }
        endings
    }

    fun List<ProgramEntity>.asProgramFromEntities() =
        this.map { item ->
            item.toProgram()
        }

    fun List<AvailableChannelResponse>.toAvailableChannelEntities() = this.map { item ->
        item.toEntity()
    }

    fun AvailableChannel.asAlreadySelected(state: Boolean) = with(this) {
        AvailableChannel(
            channelName = channelName,
            channelIcon = channelIcon,
            channelId = channelId,
            isSelected = state
        )
    }

    fun List<AvailableChannelEntity>.asChannelsFromEntities() = this.map { item ->
        item.toAvailableChannel()
    }

    fun List<SelectedChannelEntity>.asSelectedChannelsFromEntities() = this.map { item ->
        item.toSelectedChannel()
    }

    fun List<SelectedChannel>.asSelectedChannelsEntitiesFromChannels() = this.map { item ->
        SelectedChannelEntity(
            channelId = item.channelId,
            channelName = item.channelName,
            channelIcon = item.channelIcon,
            order = item.order,
            parentList = item.parentList
        )
    }


    fun List<UserChannelsListEntity>.asUserChannelsLists() = this.map { item ->
        item.toUserChannelsList()
    }
}
