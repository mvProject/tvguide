package com.mvproject.tvprogramguide.data.entity

import androidx.room.Entity
import com.mvproject.tvprogramguide.utils.DbConstants.SELECTED_CHANNEL_KEY_CHANNEL_ID
import com.mvproject.tvprogramguide.utils.DbConstants.SELECTED_CHANNEL_KEY_PARENT_LIST
import com.mvproject.tvprogramguide.utils.DbConstants.TABLE_SELECTED_CHANNELS

@Entity(
    tableName = TABLE_SELECTED_CHANNELS,
    primaryKeys = [SELECTED_CHANNEL_KEY_CHANNEL_ID, SELECTED_CHANNEL_KEY_PARENT_LIST]
)
data class SelectedChannelEntity(
    val channel_id: String,
    val channel_name: String,
    val channel_icon: String,
    val order: Int,
    val parentList: String
)
