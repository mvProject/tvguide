package com.mvproject.tvprogramguide.domain.utils

import com.mvproject.tvprogramguide.data.model.response.AvailableChannelResponse

fun List<AvailableChannelResponse>.filterNoEpg() =
    this.filterNot {
        it.channelNames.contains("No Epg", true) ||
                it.channelNames.contains("Заглушка", true)
    }