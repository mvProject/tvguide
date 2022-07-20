package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.Entity
import com.mvproject.tvprogramguide.data.database.DbConstants.PROGRAM_KEY_DATE_TIME
import com.mvproject.tvprogramguide.data.database.DbConstants.PROGRAM_KEY_TITLE
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_PROGRAMS
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.utils.correctTimeZone
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

@Entity(tableName = TABLE_PROGRAMS, primaryKeys = [PROGRAM_KEY_DATE_TIME, PROGRAM_KEY_TITLE])
data class ProgramEntity(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING,
    val channelId: String = NO_VALUE_STRING
) {
    fun toProgram() = with(this) {
        Program(
            dateTimeStart = dateTimeStart.correctTimeZone(),
            dateTimeEnd = dateTimeEnd.correctTimeZone(),
            title = title,
            description = description,
            category = category,
            channel = channelId,
        )
    }
}
