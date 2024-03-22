package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.parseChannelName

data class SelectedChannelWithIconEntity(
    @Embedded val channel: SelectedChannelEntity,
    @Relation(
        parentColumn = "channel_id",
        entityColumn = "channel_id",
    )
    val allChannel: AvailableChannelEntity?,
) {
    fun toSelectedChannel() =
        with(this) {
            SelectedChannel(
                channelId = channel.channelId,
                channelName = allChannel?.channelName?.parseChannelName() ?: NO_VALUE_STRING,
                channelIcon = allChannel?.channelIcon ?: NO_VALUE_STRING,
                order = channel.order,
                parentList = channel.parentList,
            )
        }
}
