package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_ALL_CHANNELS
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel

@Entity(tableName = TABLE_ALL_CHANNELS)
data class AvailableChannelEntity(
    @PrimaryKey
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "channel_name") val channelName: String,
    @ColumnInfo(name = "channel_icon") val channelIcon: String
) {
    fun toAvailableChannel() = with(this) {
        AvailableChannel(
            channelId = channelId,
            channelName = channelName,
            channelIcon = channelIcon
        )
    }
}
