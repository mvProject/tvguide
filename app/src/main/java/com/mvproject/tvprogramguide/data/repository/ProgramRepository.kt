package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toEntity
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.utils.TimeUtils
import com.mvproject.tvprogramguide.utils.TimeUtils.correctTimeZone
import javax.inject.Inject

class ProgramRepository
@Inject
constructor(
    private val programDao: ProgramDao,
) {
    suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> {
        return programDao
            .getSelectedChannelPrograms(
                timeStamp = TimeUtils.actualDate,
                selectedIds = channelsIds
            )
            .asProgramFromEntities()
    }


    suspend fun loadProgramsForChannel(channelId: String): List<Program> {
        return programDao
            .getChannelProgramsById(
                timeStamp = TimeUtils.actualDate,
                channelId = channelId
            )
            .asProgramFromEntities()
    }

    @Transaction
    suspend fun updatePrograms(
        channelId: String,
        programs: List<ProgramDTO>,
    ) {
        val entities = programs.map { item ->
            item
                .asProgramEntity(id = channelId)
                .correctTimeZone()
        }

        programDao.apply {
            deletePrograms(channelId = channelId)
            insertPrograms(programs = entities)
        }
    }

    suspend fun cleanProgramsBeforeDate(date: Long) {
        programDao.deleteProgramsByDate(timeStamp = date)
    }

    suspend fun updateProgram(program: Program) {
        programDao.updateProgram(programForUpdate = program.toEntity())
    }
}
