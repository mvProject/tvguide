package com.mvproject.tvprogramguide.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_list")
class CustomListEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)