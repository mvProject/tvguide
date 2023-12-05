package com.mvproject.tvprogramguide.data.mappers

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import com.mvproject.tvprogramguide.data.model.entity.UserChannelsListEntity
import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramResponse
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_LONG
import com.mvproject.tvprogramguide.utils.TimeUtils
import com.mvproject.tvprogramguide.utils.calculateEndings
import com.mvproject.tvprogramguide.utils.convertDateToReadableFormat
import com.mvproject.tvprogramguide.utils.getLastItemEnding
import com.mvproject.tvprogramguide.utils.takeIfCountNotEmpty
import com.mvproject.tvprogramguide.utils.toMillis

/**
 * Maps Data between models
 */
object Mappers {
    /**
     * Maps List of [Program] from Domain to grouped list [SingleChannelWithPrograms].
     *
     * @return the converted object
     */
    fun List<Program>.toSingleChannelWithPrograms(): List<SingleChannelWithPrograms> {
        val programs = this.groupBy { program ->
            program.dateTimeStart.convertDateToReadableFormat()
        }
        return buildList {
            programs.forEach { (date, list) ->
                add(
                    SingleChannelWithPrograms(
                        date = date,
                        programs = list
                    )
                )
            }
        }
    }

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
        alreadySelected: List<SelectedChannel>,
        itemsCount: Int = COUNT_ZERO
    ): List<SelectedChannelWithPrograms> {
        val programs = this.groupBy { program ->
            program.channel
        }
        return buildList {
            alreadySelected.forEach { chn ->
                programs[chn.channelId]?.let { prg ->
                    if (prg.count() > COUNT_ZERO) {
                        add(
                            SelectedChannelWithPrograms(
                                selectedChannel = chn,
                                programs = prg.takeIfCountNotEmpty(count = itemsCount)
                            )
                        )
                    }
                }
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

    /**
     * Maps List of [ProgramResponse] from Response to Entity
     * with id and filter for current day
     *
     * @param channelId id of channel for program
     *
     * @return the converted object
     */
    fun List<ProgramResponse>.asProgramEntities(
        channelId: String
    ): List<ProgramEntity> {
        val actualDayFiltered = this.filterToActualDay()

        val programEndings = actualDayFiltered
            .map { programResponse -> programResponse.start }
            .calculateEndings()

        return actualDayFiltered.mapIndexed { index, item ->
            val endingTime = programEndings.elementAtOrNull(index)
                ?: item.start.toMillis().getLastItemEnding()
            item.asProgramEntity(
                channelId = channelId,
                endTime = endingTime
            )
        }
    }

    /**
     * Filter List of [ProgramResponse] for current day programs start
     *
     * @return the filtered list
     */
    private fun List<ProgramResponse>.filterToActualDay(): List<ProgramResponse> {
        val actual = this.filter { programResponse ->
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
    fun List<AvailableChannelResponse>.toAvailableChannelEntities() = this.map { item ->
        item.toEntity()
    }

    /**
     * Maps List of [AvailableChannelEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<AvailableChannelEntity>.asChannelsFromEntities() = this.map { item ->
        item.toAvailableChannel()
    }

    /**
     * Maps List of [SelectedChannelEntity] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<SelectedChannelEntity>.asSelectedChannelsFromEntities() = this.map { item ->
        item.toSelectedChannel()
    }

    fun List<SelectedChannelWithIconEntity>.asSelectedChannelsFromAltEntities() = this.map { item ->
        item.toSelectedChannel()
    }

    /**
     * Maps List of [SelectedChannel] from Domain to Entity.
     *
     * @return the converted object
     */
    fun List<SelectedChannel>.asSelectedChannelsEntitiesFromChannels() = this.map { item ->
        SelectedChannelEntity(
            channelId = item.channelId,
            channelName = item.channelName,
            order = item.order,
            parentList = item.parentList
        )
    }

    /**
     * Maps List of [UserChannelsList] from Entity to Domain.
     *
     * @return the converted object
     */
    fun List<UserChannelsListEntity>.asUserChannelsLists() = this.map { item ->
        item.toUserChannelsList()
    }
}
