package com.mvproject.tvprogramguide.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_USER_CUSTOM_LIST

@Entity(tableName = TABLE_USER_CUSTOM_LIST)
class ChannelsListEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isSelected: Boolean,
)
