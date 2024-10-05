package com.mvproject.tvprogramguide.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_ALL_CHANNELS

@Entity(tableName = TABLE_ALL_CHANNELS)
data class AvailableChannelEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val title: String,
    val logo: String,
)
