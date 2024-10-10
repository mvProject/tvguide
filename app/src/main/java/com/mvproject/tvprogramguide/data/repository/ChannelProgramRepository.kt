package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toEntity
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.response.ProgramDTO
import com.mvproject.tvprogramguide.utils.TimeUtils.correctTimeZone
import kotlinx.datetime.Clock
import javax.inject.Inject

class ChannelProgramRepository
    @Inject
    constructor(
        private val programDao: ProgramDao,
    ) {
        suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> =
            programDao
                .getSelectedChannelPrograms(selectedIds = channelsIds)
                .asProgramFromEntities()
                .filter { program ->
                    program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
                }

        suspend fun loadProgramsForChannel(channelId: String): List<Program> =
            programDao
                .getChannelProgramsById(channelId = channelId)
                .asProgramFromEntities()
                .filter { program ->
                    program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
                }

        @Transaction
        suspend fun updatePrograms(
            channelId: String,
            programs: List<ProgramDTO>,
        ) {
            val entities = programs.map { item -> item.asProgramEntity(id = channelId) }

            programDao.apply {
                deletePrograms(channelId = channelId)
                insertPrograms(programs = entities.map { it.correctTimeZone() })
            }
        }

        suspend fun updateProgram(program: Program) {
            programDao.updateProgram(programForUpdate = program.toEntity())
        }
    }
