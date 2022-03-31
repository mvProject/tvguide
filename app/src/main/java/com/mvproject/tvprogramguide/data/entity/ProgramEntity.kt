package com.mvproject.tvprogramguide.data.entity

import androidx.room.Entity
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.DbConstants.PROGRAM_KEY_DATE_TIME
import com.mvproject.tvprogramguide.utils.DbConstants.PROGRAM_KEY_TITLE
import com.mvproject.tvprogramguide.utils.DbConstants.TABLE_PROGRAMS

@Entity(tableName = TABLE_PROGRAMS, primaryKeys = [PROGRAM_KEY_DATE_TIME, PROGRAM_KEY_TITLE])
data class ProgramEntity(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING,
    val channelId: String = NO_VALUE_STRING
)