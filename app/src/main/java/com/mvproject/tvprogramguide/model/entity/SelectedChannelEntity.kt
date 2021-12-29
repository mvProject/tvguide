package com.mvproject.tvprogramguide.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_channels", primaryKeys = ["channel_id", "parentList"])
data class SelectedChannelEntity(
    val channel_id: String,
    val channel_name: String,
    val channel_icon: String,
    val parentList: String
)