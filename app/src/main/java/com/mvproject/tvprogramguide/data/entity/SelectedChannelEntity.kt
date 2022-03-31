package com.mvproject.tvprogramguide.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.utils.DbConstants.TABLE_SELECTED_CHANNELS

@Entity(tableName = TABLE_SELECTED_CHANNELS, primaryKeys = ["channel_id", "parentList"])
data class SelectedChannelEntity(
    val channel_id: String,
    val channel_name: String,
    val channel_icon: String,
    val parentList: String
)