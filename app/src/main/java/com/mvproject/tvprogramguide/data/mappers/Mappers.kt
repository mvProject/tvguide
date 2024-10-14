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
import com.mvproject.tvprogramguide.utils.parseChannelName
import com.mvproject.tvprogramguide.utils.trimSpaces
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Maps Data between models
 */
object Mappers {
    /**
     * Maps instance of [AvailableChannelResponse] from Response to Entity.
     *
     * @return the converted object
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
     * Maps List of [AvailableChannelResponse] from Response to Entity.
     *
     * @return the converted object
     */
    fun List<AvailableChannelResponse>.toAvailableChannelEntities() =
        this.map { item ->
            item.toEntity()
        }

    /**
     * Maps instance of [ProgramEntity] from Response to Entity.
     *
     * @return the converted object
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
     * Maps List of [ProgramEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<ProgramEntity>.asProgramFromEntities() =
        this.map { item ->
            item.toProgram()
        }

    fun AvailableChannelEntity.asSelectionFromAvailable() =
        with(this) {
            SelectionChannel(
                channelId = id,
                programId = programId,
                channelName = title.parseChannelName(),
                channelIcon = logo,
            )
        }

    fun SelectedChannelWithIconEntity.asSelectionFromSelected() =
        with(this) {
            SelectionChannel(
                programId = channel.programId,
                channelId = channel.id,
                channelName = allChannel?.title?.parseChannelName() ?: String.empty,
                channelIcon = allChannel?.logo ?: String.empty,
                order = channel.order,
                parentList = channel.parentList,
            )
        }

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
     * Maps instance of [ChannelsListEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    private fun ChannelsListEntity.toChannelList() =
        ChannelList(
            id = id,
            listName = name,
            isSelected = isSelected
        )

    /**
     * Maps List of [ChannelsListEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<ChannelsListEntity>.asChannelLists() =
        this.map { item ->
            item.toChannelList()
        }

    /**
     * Maps instance of [ChannelsListEntity] from Entity to Domain.
     *
     * @return the converted object
     */

    fun ChannelList.toChannelsListEntity() =
        ChannelsListEntity(
            id = id,
            name = listName,
            isSelected = isSelected
        )

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
