package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.utils.TimeUtils.correctTimeZone
import com.mvproject.tvprogramguide.utils.getNoProgramData
import kotlinx.datetime.Clock
import timber.log.Timber
import javax.inject.Inject

class ChannelProgramRepository @Inject constructor(
    private val epgService: EpgService,
    private val programDao: ProgramDao
) {
    suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> {
        return programDao.getSelectedChannelPrograms(ids = channelsIds)
            .asProgramFromEntities()
            .filter { program ->
                program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
            }
    }

    suspend fun loadProgramsForChannel(channelId: String): List<Program> {
        return programDao.getSingleChannelPrograms(channelId = channelId)
            .asProgramFromEntities()
            .filter { program ->
                program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
            }
    }

    suspend fun loadProgramsCount(channelsIds: List<String>): Int {
        return programDao.getSelectedChannelPrograms(ids = channelsIds)
            .groupBy { program ->
                program.channelId
            }
            .keys
            .count()
    }

    @Transaction
    suspend fun loadProgram(channelId: String) {
        val entities = try {
            val prg = epgService.getChannelProgram(channelId)
            val ch = prg.chPrograms
            if (ch.isEmpty()) {
                channelId.getNoProgramData()
            } else {
                ch.asProgramEntities(channelId = channelId)
            }
        } catch (ex: Exception) {
            Timber.e("testing loadProgram for $channelId IllegalStateException  ${ex.localizedMessage}")
            channelId.getNoProgramData()
        }
        programDao.apply {
            deletePrograms(channelId = channelId)
            insertPrograms(
                channels = entities.map {
                    it.correctTimeZone()
                }
            )
        }
    }

    suspend fun updateProgram(program: Program) {
        programDao.updateProgram(programForUpdate = program.toEntity())
    }
}
