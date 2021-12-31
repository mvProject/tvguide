package com.mvproject.tvprogramguide.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_list")
class CustomListEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)