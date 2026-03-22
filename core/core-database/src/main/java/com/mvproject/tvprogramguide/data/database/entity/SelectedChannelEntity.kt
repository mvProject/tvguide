package com.mvproject.tvprogramguide.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_SELECTED_CHANNELS
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Entity(tableName = TABLE_SELECTED_CHANNELS)
data class SelectedChannelEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val title: String,
    val order: Int = COUNT_ZERO,
    val parentList: String = String.empty,
)
