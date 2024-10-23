package com.mvproject.tvprogramguide.data.database.entity

import androidx.room.Entity
import com.mvproject.tvprogramguide.data.database.DbConstants.PROGRAM_KEY_DATE_TIME
import com.mvproject.tvprogramguide.data.database.DbConstants.PROGRAM_KEY_TITLE
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_PROGRAMS
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Entity(tableName = TABLE_PROGRAMS, primaryKeys = [PROGRAM_KEY_DATE_TIME, PROGRAM_KEY_TITLE])
data class ProgramEntity(
    val programId:String,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
    val category: String = String.empty,
    val channelId: String = String.empty,
    val scheduledId: Long? = null,
)