package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.database.entity.ChannelEntity
import com.mvproject.tvprogramguide.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.model.data.SingleChannelProgramList
import com.mvproject.tvprogramguide.netwotk.json.JsonChannelModel
import com.mvproject.tvprogramguide.netwotk.json.JsonProgram
import com.mvproject.tvprogramguide.utils.Utils.correctTimeZone
import com.mvproject.tvprogramguide.utils.Utils.toMillis

object Mappers {
    fun List<Program>.toSortedSingleChannelPrograms(): List<SingleChannelProgramList> {
        val sortedPrograms = mutableListOf<SingleChannelProgramList>()
        this.groupBy { it.dateTimeStart }.forEach { (date, list) ->
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
            programs[chn.channelId]?.let { prg ->
                if (prg.count() > COUNT_ZERO) {
                    sortedPrograms.add(chn)
                    sortedPrograms.addAll(
                        if (itemsCount > COUNT_ZERO)
                            prg.take(itemsCount)
                        else prg
                    )
                }
            }
        }
        return sortedPrograms
    }

    private fun JsonProgram.asProgramEntity(
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


    fun List<JsonProgram>.asProgramEntities(
        channelId: String
    ): List<ProgramEntity> {
        val actual = this.filter { it.start.toMillis() > Utils.actualDay }
        val filtered = if (actual.count()> COUNT_ZERO) actual else this
        val endings = filtered.calculateEndings()
        return filtered.mapIndexed { index, item ->
            val endingTime = endings.elementAtOrNull(index) ?: COUNT_ZERO_LONG
            item.asProgramEntity(channelId, endingTime)
        }
    }


    private fun List<JsonProgram>.calculateEndings(): List<Long> = with(this) {
        val endings = mutableListOf<Long>()
        zipWithNext().forEach { p ->
            endings.add(p.second.start.toMillis())
        }
        endings
    }

    private fun ProgramEntity.asProgramFromEntity(channelId: String) =
        with(this) {
            Program(
                dateTimeStart = dateTimeStart.correctTimeZone(),
                dateTimeEnd = dateTimeEnd.correctTimeZone(),
                title = title,
                description = description,
                category = category,
                channel = channelId,
            )
        }

    private fun ProgramEntity.asProgramFromEntity() =
        with(this) {
            Program(
                dateTimeStart = dateTimeStart.correctTimeZone(),
                dateTimeEnd = dateTimeEnd.correctTimeZone(),
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
