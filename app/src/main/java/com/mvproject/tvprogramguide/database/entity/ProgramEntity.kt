package com.mvproject.tvprogramguide.database.entity

import androidx.room.Entity

@Entity(tableName = "programs", primaryKeys = ["dateTimeStart", "title"])
data class ProgramEntity(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val channelId: String = ""
)