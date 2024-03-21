package com.mvproject.tvprogramguide.utils

import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE

object ChannelUtils {
    fun List<SelectionChannel>.updateOrders() =
        this.mapIndexed { ind, chn ->
            chn.copy(order = ind + COUNT_ONE)
        }
}
