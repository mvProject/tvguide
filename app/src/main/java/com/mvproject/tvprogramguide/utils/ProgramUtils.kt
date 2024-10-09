package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO

object ProgramUtils {
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

                val currentPrograms = programs[chn.programId] ?: emptyList()

                add(
                    SelectedChannelWithPrograms(
                        selectedChannel = chn,
                        programs = currentPrograms.takeIfCountNotEmpty(count = itemsCount),
                    ),
                )
            }
        }
    }
}