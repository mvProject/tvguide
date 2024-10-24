package com.mvproject.tvprogramguide.data.mappers

import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import com.mvproject.tvprogramguide.utils.trimSpaces
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Mappers object contains extension functions for mapping between different data models.
 */
object Mappers {
    /**
     * Maps an instance of [AvailableChannelResponse] to [AvailableChannelEntity].
     *
     * @return the converted [AvailableChannelEntity] object
     */
    private fun AvailableChannelResponse.toEntity() =
        with(this) {
            AvailableChannelEntity(
                title = channelNames,
                logo = channelIcon,
                programId = channelId,
                id = ("$channelId$channelNames").trimSpaces(),
            )
        }

    /**
     * Maps a List of [AvailableChannelResponse] to a List of [AvailableChannelEntity].
     *
     * @return the converted list of [AvailableChannelEntity] objects
     */
    fun List<AvailableChannelResponse>.toAvailableChannelEntities() =
        this.map { item ->
            item.toEntity()
        }

    /**
     * Maps an instance of [ProgramEntity] to [Program].
     *
     * @return the converted [Program] object
     */
    private fun ProgramEntity.toProgram() =
        with(this) {
            Program(
                programId = programId,
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                category = category,
                channel = channelId,
                scheduledId = scheduledId,
            )
        }
    /**
     * Maps a List of [ProgramEntity] to a List of [Program].
     *
     * @return the converted list of [Program] objects
     */
    fun List<ProgramEntity>.asProgramFromEntities() =
        this.map { item ->
            item.toProgram()
        }

    /**
     * Maps an [AvailableChannelEntity] to [SelectionChannel].
     *
     * @return the converted [SelectionChannel] object
     */
    fun AvailableChannelEntity.asSelectionFromAvailable() =
        with(this) {
            SelectionChannel(
                channelId = id,
                programId = programId,
                channelName = title,
                channelIcon = logo,
            )
        }

    /**
     * Maps a [SelectedChannelWithIconEntity] to [SelectionChannel].
     *
     * @return the converted [SelectionChannel] object
     */
    fun SelectedChannelWithIconEntity.asSelectionFromSelected() =
        with(this) {
            SelectionChannel(
                programId = channel.programId,
                channelId = channel.id,
                channelName = allChannel?.title ?: String.empty,
                channelIcon = allChannel?.logo ?: String.empty,
                order = channel.order,
                parentList = channel.parentList,
            )
        }

    /**
     * Maps a [ProgramDTO] to [ProgramEntity] with a given channel ID.
     *
     * @param id The channel ID to associate with the program
     * @return the converted [ProgramEntity] object
     */
    @OptIn(ExperimentalUuidApi::class)
    fun ProgramDTO.asProgramEntity(id: String) =
        with(this) {
            ProgramEntity(
                programId = Uuid.random().toString(),
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                channelId = id,
            )
        }

    /**
     * Maps a List of [SelectionChannel] to a List of [SelectedChannelEntity].
     *
     * @return the converted list of [SelectedChannelEntity] objects
     */
    fun List<SelectionChannel>.asSelectionChannelToEntity() =
        this.map { item ->
            SelectedChannelEntity(
                id = item.channelId,
                programId = item.programId,
                title = item.channelName,
                order = item.order,
                parentList = item.parentList,
            )
        }

    /**
     * Maps an instance of [ChannelsListEntity] to [ChannelList].
     *
     * @return the converted [ChannelList] object
     */
    private fun ChannelsListEntity.toChannelList() =
        ChannelList(
            id = id,
            listName = name,
            isSelected = isSelected
        )

    /**
     * Maps a List of [ChannelsListEntity] to a List of [ChannelList].
     *
     * @return the converted list of [ChannelList] objects
     */
    fun List<ChannelsListEntity>.asChannelLists() =
        this.map { item ->
            item.toChannelList()
        }

    /**
     * Maps an instance of [ChannelList] to [ChannelsListEntity].
     *
     * @return the converted [ChannelsListEntity] object
     */
    fun ChannelList.toChannelsListEntity() =
        ChannelsListEntity(
            id = id,
            name = listName,
            isSelected = isSelected
        )

    /**
     * Maps an instance of [Program] to [ProgramEntity].
     *
     * @return the converted [ProgramEntity] object
     */
    fun Program.toEntity() =
        with(this) {
            ProgramEntity(
                programId = programId,
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                category = category,
                channelId = channel,
                scheduledId = scheduledId,
            )
        }
}
