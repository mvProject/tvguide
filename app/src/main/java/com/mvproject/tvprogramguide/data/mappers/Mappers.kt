package com.mvproject.tvprogramguide.data.mappers

import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramResponse2
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.TimeUtils
import com.mvproject.tvprogramguide.utils.parseChannelName
import com.mvproject.tvprogramguide.utils.takeIfCountNotEmpty
import com.mvproject.tvprogramguide.utils.toMillis
import com.mvproject.tvprogramguide.utils.toNoProgramData

/**
 * Maps Data between models
 */
object Mappers {
    /**
     * Maps List of [Program] from Domain to grouped list [SelectedChannelWithPrograms].
     * with itemsCount
     *
     * @param alreadySelected list of selected channels
     * @param itemsCount programs count for view
     *
     * @return the converted object
     */
    fun List<Program>.toSelectedChannelWithPrograms(
        alreadySelected: List<SelectionChannel>,
        itemsCount: Int = COUNT_ZERO,
    ): List<SelectedChannelWithPrograms> {
        val programs =
            this.groupBy { program ->
                program.channel
            }

        return buildList {
            alreadySelected.forEach { chn ->

                val currentPrograms = programs[chn.programId]

                val channelPrograms =
                    if (!currentPrograms.isNullOrEmpty()) {
                        currentPrograms
                    } else {
                        // todo modify cause not update if new added
                        chn.channelId.toNoProgramData()
                    }

                add(
                    SelectedChannelWithPrograms(
                        selectedChannel = chn,
                        programs = channelPrograms.takeIfCountNotEmpty(count = itemsCount),
                    ),
                )
            }
        }
    }
    /**
     * Maps List of [ProgramResponse] from Response to Entity.
     * with id and end time
     *
     * @param channelId id of channel for program
     * @param endTime time in milliseconds for program end
     *
     * @return the converted object
     */
    /*    private fun ProgramResponse.asProgramEntity(
            channelId: String,
            endTime: Long = COUNT_ZERO_LONG,
        ) = with(this) {
            ProgramEntity(
                dateTimeStart = start.toMillis(),
                dateTimeEnd = endTime,
                title = title,
                description = description,
                category = category,
                channelId = channelId,
            )
        }*/

    /**
     * Maps List of [ProgramResponse] from Response to Entity
     * with id and filter for current day
     *
     * @param channelId id of channel for program
     *
     * @return the converted object
     */
    /*  fun List<ProgramResponse>.asProgramEntities(channelId: String): List<ProgramEntity> {
          val actualDayFiltered = this.filterToActualDay()

          val programEndings =
              actualDayFiltered
                  .map { programResponse -> programResponse.start }
                  .calculateEndings()

          return actualDayFiltered.mapIndexed { index, item ->
              val endingTime =
                  programEndings.elementAtOrNull(index)
                      ?: item.start.toMillis().getLastItemEnding()
              item.asProgramEntity(
                  channelId = channelId,
                  endTime = endingTime,
              )
          }
      }*/

    /**
     * Filter List of [ProgramResponse] for current day programs start
     *
     * @return the filtered list
     */
    private fun List<ProgramResponse>.filterToActualDay(): List<ProgramResponse> {
        val actual =
            this.filter { programResponse ->
                programResponse.start.toMillis() > TimeUtils.actualDay
            }
        return if (actual.count() > COUNT_ZERO) actual else this
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
     * Maps List of [AvailableChannelEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    /*
        fun List<AvailableChannelEntity>.asChannelsFromEntities() =
            this.map { item ->
                item.toAvailableChannel()
            }
     */

    /**
     * Maps List of [SelectedChannelEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    /*    fun List<SelectedChannelEntity>.asSelectedChannelsFromEntities() =
            this.map { item ->
                item.toSelectedChannel()
            }*/

    /*    fun List<SelectedChannelWithIconEntity>.asSelectedChannelsFromAltEntities() =
            this.map { item ->
                item.toSelectedChannel()
            }*/

    /*    private fun AvailableChannel.asSelectionFromAvailable() =
            with(this) {
                SelectionChannel(
                    channelId = channelId,
                    channelName = channelName.parseChannelName(),
                    channelIcon = channelIcon,
                )
            }*/

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
                channelName = allChannel?.title?.parseChannelName() ?: NO_VALUE_STRING,
                channelIcon = allChannel?.logo ?: NO_VALUE_STRING,
                order = channel.order,
                parentList = channel.parentList,
            )
        }

    fun ProgramResponse2.asProgramEntity(id: String) =
        with(this) {
            ProgramEntity(
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                channelId = id,
            )
        }

    /*    fun List<AvailableChannel>.asAvailableSelectionChannels() =
            this.map { item ->
                item.asSelectionFromAvailable()
            }

        fun List<SelectedChannelWithIconEntity>.asSelectionChannels() =
            this.map { item ->
                item.asSelectionFromSelected()
            }*/

    /**
     * Maps List of [SelectedChannel] from Domain to Entity.
     *
     * @return the converted object
     */
    /*    fun List<SelectedChannel>.asSelectedChannelsEntitiesFromChannels() =
            this.map { item ->
                SelectedChannelEntity(
                    channelId = item.channelId,
                    channelName = item.channelName,
                    order = item.order,
                    parentList = item.parentList,
                )
            }*/

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
     * Maps List of [ChannelList] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<ChannelsListEntity>.asChannelLists() =
        this.map { item ->
            item.toChannelList()
        }

    private fun ChannelsListEntity.toChannelList() =
        ChannelList(
            id = id,
            listName = name,
            isSelected = isSelected
        )

    fun ChannelList.toChannelsListEntity() =
        ChannelsListEntity(
            id = id,
            name = listName,
            isSelected = isSelected
        )
}
