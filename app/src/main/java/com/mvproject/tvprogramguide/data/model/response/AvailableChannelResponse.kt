package com.mvproject.tvprogramguide.data.model.response

import com.google.gson.annotations.SerializedName
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity

data class AvailableChannelResponse(
    @SerializedName("chan_id")
    val channelId: String,
    @SerializedName("chan_names")
    val channelNames: String,
    @SerializedName("chan_icon")
    val channelIcon: String
) {
    fun toEntity() = with(this) {
        AvailableChannelEntity(
            channelName = channelNames,
            channelIcon = channelIcon,
            channelId = channelId
        )
    }
}
