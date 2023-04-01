package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mvproject.tvprogramguide.data.database.DbConstants.SELECTED_CHANNEL_KEY_CHANNEL_ID
import com.mvproject.tvprogramguide.data.database.DbConstants.SELECTED_CHANNEL_KEY_PARENT_LIST
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_SELECTED_CHANNELS
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.data.utils.parseChannelName

@Entity(
    tableName = TABLE_SELECTED_CHANNELS,
    primaryKeys = [SELECTED_CHANNEL_KEY_CHANNEL_ID, SELECTED_CHANNEL_KEY_PARENT_LIST]
)
data class SelectedChannelEntity(
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "channel_name") val channelName: String,
    val order: Int = COUNT_ZERO,
    val parentList: String = NO_VALUE_STRING
) {
    fun toSelectedChannel() = with(this) {
        SelectedChannel(
            channelId = channelId,
            channelName = channelName.parseChannelName(),
            order = order,
            parentList = parentList
        )
    }
}
