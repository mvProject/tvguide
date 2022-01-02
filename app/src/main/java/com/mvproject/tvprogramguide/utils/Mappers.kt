package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.model.data.SingleChannelProgramList
import com.mvproject.tvprogramguide.database.entity.ChannelEntity
import com.mvproject.tvprogramguide.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.netwotk.json.JsonChannelModel
import com.mvproject.tvprogramguide.netwotk.json.JsonProgram
import com.mvproject.tvprogramguide.utils.Utils.correctTimeZone
import com.mvproject.tvprogramguide.utils.Utils.toMillis

object Mappers {
    fun List<Program>.toSortedSingleChannelPrograms(): List<SingleChannelProgramList> {
        val sortedPrograms = mutableListOf<SingleChannelProgramList>()
        this.groupBy { it.dateTime }.forEach { (date, list) ->
            sortedPrograms.add(
                SingleChannelProgramList(date, list)
            )
        }
        return sortedPrograms
    }

    fun List<Program>.toSortedSelectedChannelsPrograms(
        alreadySelected: List<Channel>,
        itemsCount: Int = COUNT_ZERO
    ): List<IChannel> {
        val sortedPrograms = mutableListOf<IChannel>()
        val programs = this.groupBy { it.channel }

        alreadySelected.forEach { chn ->
            programs[chn.channelId]?.let {
                sortedPrograms.add(chn)
                sortedPrograms.addAll(
                    if (itemsCount > COUNT_ZERO)
                        it.take(itemsCount)
                    else it
                )
            }
        }
        return sortedPrograms
    }

    private fun JsonProgram.asProgramEntity(
        channelId: String,
        durationTime: Long = COUNT_ZERO_LONG
    ) =
        with(this) {
            ProgramEntity(
                dateTime = start.toMillis().correctTimeZone(),
                duration = durationTime,
                title = title,
                description = description,
                category = category,
                channelId = channelId
            )
        }


    fun List<JsonProgram>.asProgramEntities(
        channelId: String
    ) =
        this.filter { it.start.toMillis().correctTimeZone() > Utils.actualDay }
            .mapIndexed { index, item ->
                val durations = this.calculateDurations()
                val durationTime = durations.elementAtOrNull(index) ?: COUNT_ZERO_LONG
                item.asProgramEntity(channelId, durationTime)
            }

    private fun List<JsonProgram>.calculateDurations(): List<Long> = with(this) {
        val durations = mutableListOf<Long>()
        zipWithNext().forEach { p ->
            val duration = p.second.start.toMillis() - p.first.start.toMillis()
            durations.add(duration)
        }
        durations
    }

    private fun ProgramEntity.asProgramFromEntity(channelId: String) =
        with(this) {
            Program(
                dateTime = dateTime,
                duration = duration,
                title = title,
                description = description,
                category = category,
                channel = channelId,
            )
        }

    private fun ProgramEntity.asProgramFromEntity() =
        with(this) {
            Program(
                dateTime = dateTime,
                duration = duration,
                title = title,
                description = description,
                category = category,
                channel = channelId,
            )
        }

    fun List<ProgramEntity>.asProgramFromEntities(channelId: String) =
        this.map { item ->
            item.asProgramFromEntity(channelId)
        }

    fun List<ProgramEntity>.asProgramFromEntities() =
        this.map { item ->
            item.asProgramFromEntity()
        }


    private fun JsonChannelModel.asChannelEntity() = with(this) {
        ChannelEntity(
            channel_name = chan_names,
            channel_icon = chan_icon,
            channel_id = chan_id
        )
    }

    fun List<JsonChannelModel>.asChannelEntities() = this.map { item ->
        item.asChannelEntity()
    }

    private fun ChannelEntity.asChannelFromEntity() = with(this) {
        Channel(
            channelName = channel_name,
            channelIcon = channel_icon,
            channelId = channel_id
        )
    }

    fun Channel.asAlreadySelected(state: Boolean) = with(this) {
        Channel(
            channelName = channelName,
            channelIcon = channelIcon,
            channelId = channelId,
            isSelected = state
        )
    }

    fun List<ChannelEntity>.asChannelsFromEntities() = this.map { item ->
        item.asChannelFromEntity()
    }

    fun List<JsonChannelModel>.filterNoEpg() =
        this.filterNot {
            it.chan_names.contains("No Epg", true) ||
                    it.chan_names.contains("Заглушка", true)
        }

}
