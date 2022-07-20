package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mvproject.tvprogramguide.data.database.DbConstants.SELECTED_CHANNEL_KEY_CHANNEL_ID
import com.mvproject.tvprogramguide.data.database.DbConstants.SELECTED_CHANNEL_KEY_PARENT_LIST
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_SELECTED_CHANNELS
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel

@Entity(
    tableName = TABLE_SELECTED_CHANNELS,
    primaryKeys = [SELECTED_CHANNEL_KEY_CHANNEL_ID, SELECTED_CHANNEL_KEY_PARENT_LIST]
)
data class SelectedChannelEntity(
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "channel_name") val channelName: String,
    @ColumnInfo(name = "channel_icon") val channelIcon: String,
    val order: Int = 0,
    val parentList: String = ""
) {
    fun toSelectedChannel() = with(this) {
        SelectedChannel(
            channelId = channelId,
            channelName = channelName,
            channelIcon = channelIcon,
            order = order,
            parentList = parentList
        )
    }
}
