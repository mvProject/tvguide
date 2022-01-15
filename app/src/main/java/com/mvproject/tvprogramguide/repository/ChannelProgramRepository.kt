package com.mvproject.tvprogramguide.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.database.dao.ProgramDao
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.utils.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.Mappers.asProgramEntities
import com.mvproject.tvprogramguide.utils.Mappers.asProgramFromEntities
import javax.inject.Inject

class ChannelProgramRepository @Inject constructor(
    private val epgService: EpgService,
    private val programDao: ProgramDao
) {
    suspend fun loadPrograms(channels: List<String>): List<Program> {
        return programDao.getSelectedChannelPrograms(channels)
            .asProgramFromEntities()
            .filter { it.dateTimeEnd > System.currentTimeMillis() }
    }

    suspend fun loadProgramsCount(channels: List<String>): Int {
        return programDao.getSelectedChannelPrograms(channels).groupBy { it.channelId }.keys.count()
    }

    @Transaction
    suspend fun loadProgram(id: String) {
        val ch = epgService.getChannelProgram(id).ch_programme
        val entities = ch.asProgramEntities(id)
        if (entities.count() > COUNT_ZERO) {
            programDao.apply {
                deletePrograms(id)
                insertPrograms(entities)
            }
        }
    }
}