package com.mvproject.tvprogramguide.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.utils.DbConstants.TABLE_ALL_CHANNELS

@Entity(tableName = TABLE_ALL_CHANNELS)
data class ChannelEntity(
    @PrimaryKey
    val channel_id: String,
    val channel_name: String,
    val channel_icon: String
)
