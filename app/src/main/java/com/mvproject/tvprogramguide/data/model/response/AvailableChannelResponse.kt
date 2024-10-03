package com.mvproject.tvprogramguide.data.model.response

import com.google.gson.annotations.SerializedName
import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity

data class AvailableChannelResponse(
    @SerializedName("chan_id")
    val channelId: String,
    @SerializedName("chan_names")
    val channelNames: String,
    @SerializedName("chan_icon")
    val channelIcon: String,
) {
    fun toEntity() =
        with(this) {
            AvailableChannelEntity(
                title = channelNames,
                logo = channelIcon,
                programId = channelId,
                id = ("$channelId$channelNames").replace(" ", ""),
            )
        }
}
