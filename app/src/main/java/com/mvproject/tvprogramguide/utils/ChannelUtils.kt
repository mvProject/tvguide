package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE

/**
 * Utility object providing helper functions for channel-related operations.
 */
object ChannelUtils {
    /**
     * Extension function to update the order of SelectionChannel items in a list.
     *
     * This function assigns a new order to each channel based on its position in the list.
     * The order starts from 1 and increments for each subsequent item.
     *
     * @receiver List<SelectionChannel> The list of SelectionChannel objects to be updated.
     * @return List<SelectionChannel> A new list with updated order values for each channel.
     */
    fun List<SelectionChannel>.updateOrders() =
        this.mapIndexed { ind, chn ->
            chn.copy(order = ind + COUNT_ONE)
        }
}
