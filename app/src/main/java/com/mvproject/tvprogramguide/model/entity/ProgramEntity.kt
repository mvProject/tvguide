package com.mvproject.tvprogramguide.model.entity

import androidx.room.Entity

@Entity(tableName = "programs", primaryKeys = ["dateTime", "title"])
data class ProgramEntity(
    val dateTime: Long,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val channelId: String = "",
    val selectedId: String = ""
)