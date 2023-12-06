package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.utils.parseChannelName

data class SelectedChannelWithIconEntity(
    @Embedded val channel: SelectedChannelEntity,
    @Relation(
        parentColumn = "channel_id",
        entityColumn = "channel_id"
    )
    val allChannel: AvailableChannelEntity?
) {
    fun toSelectedChannel() = with(this) {
        SelectedChannel(
            channelId = channel.channelId,
            channelName = allChannel?.channelName?.parseChannelName() ?: "",
            channelIcon = allChannel?.channelIcon ?: "",
            order = channel.order,
            parentList = channel.parentList
        )
    }
}
