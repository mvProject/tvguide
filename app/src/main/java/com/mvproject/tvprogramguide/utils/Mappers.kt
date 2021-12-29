package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.model.data.SingleChannelProgramList
import com.mvproject.tvprogramguide.model.entity.ChannelEntity
import com.mvproject.tvprogramguide.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.model.json.JsonChannelModel
import com.mvproject.tvprogramguide.model.json.JsonProgram
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
        itemsCount: Int = 0
    ): List<IChannel> {
        val sortedPrograms = mutableListOf<IChannel>()
        val programs = this.groupBy { it.channel }

        alreadySelected.forEach { chn ->
            programs[chn.channelId]?.let {
                sortedPrograms.add(chn)
                sortedPrograms.addAll(
                    if (itemsCount > 0)
                        it.take(itemsCount)
                    else it
                )
            }
        }
        return sortedPrograms
    }

    private fun JsonProgram.asProgramFromJson() = with(this) {
        Program(
            dateTime = start.toMillis().correctTimeZone(),
            title = title,
            description = description,
            category = category
        )
    }

    private fun JsonProgram.asProgramFromJson(channelId: String) = with(this) {
        Program(
            dateTime = start.toMillis().correctTimeZone(),
            title = title,
            description = description,
            category = category,
            channel = channelId
        )
    }

    fun List<JsonProgram>.asProgramsFromJson() = this.map { item ->
        item.asProgramFromJson()
    }

    fun List<JsonProgram>.asProgramsFromJson(channelId: String) = this.map { item ->
        item.asProgramFromJson(channelId)
    }

    private fun JsonProgram.asProgramEntity(channelId: String, selectedId: String = "") =
        with(this) {
            ProgramEntity(
                dateTime = start.toMillis().correctTimeZone(),
                title = title,
                description = description,
                category = category,
                channelId = channelId,
                selectedId = selectedId
            )
        }


    fun List<JsonProgram>.asProgramEntities(channelId: String, selectedId: String = "") =
        this.map { item ->
            item.asProgramEntity(channelId, selectedId)
        }

    private fun ProgramEntity.asProgramFromEntity() = with(this) {
        Program(
            dateTime = dateTime,
            title = title,
            description = description,
            category = category
        )
    }

    fun List<ProgramEntity>.asProgramFromEntities() = this.map { item ->
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

    private fun JsonChannelModel.asChannelFromJson() = with(this) {
        Channel(
            channelName = chan_names,
            channelIcon = chan_icon,
            channelId = chan_id
        )
    }

    fun List<JsonChannelModel>.asChannelsFromJsons() = this.map { item ->
        item.asChannelFromJson()
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
