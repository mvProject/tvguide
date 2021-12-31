package com.mvproject.tvprogramguide.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_channels")
data class ChannelEntity(
    @PrimaryKey
    val channel_id: String,
    val channel_name: String,
    val channel_icon: String
)