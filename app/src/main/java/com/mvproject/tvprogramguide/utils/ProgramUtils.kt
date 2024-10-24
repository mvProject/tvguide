package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
/**
 * Utility object providing helper functions for program-related operations.
 */
object ProgramUtils {
    /**
     * Extension function to convert a list of Programs to a list of SelectedChannelWithPrograms.
     * This function groups programs by channel and combines them with the list of selected channels.
     *
     * @receiver List<Program> The list of programs to be grouped and converted.
     * @param alreadySelected List of channels that are already selected.
     * @param itemsCount The number of program items to include for each channel. If 0, all programs are included.
     * @return List<SelectedChannelWithPrograms> A list of SelectedChannelWithPrograms objects,
     *         each containing a selected channel and its associated programs.
     */
    fun List<Program>.toSelectedChannelWithPrograms(
        alreadySelected: List<SelectionChannel>,
        itemsCount: Int = COUNT_ZERO,
    ): List<SelectedChannelWithPrograms> {
        // Group programs by channel
        val programs =
            this.groupBy { program ->
                program.channel
            }

        //return buildList {
        return alreadySelected.map { chn ->

                val currentPrograms = programs[chn.programId] ?: emptyList()

               // add(
                    SelectedChannelWithPrograms(
                        selectedChannel = chn,
                        // Take a subset of programs if itemsCount is specified, otherwise take all
                        programs = currentPrograms.takeIfCountNotEmpty(count = itemsCount),
                    )
               // )
            }
       // }
    }
}