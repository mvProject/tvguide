package com.mvproject.tvprogramguide.data.repository

import android.text.format.DateUtils
import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.data.utils.Mappers.asProgramEntities
import com.mvproject.tvprogramguide.data.utils.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import javax.inject.Inject

class ChannelProgramRepository @Inject constructor(
    private val epgService: EpgService,
    private val programDao: ProgramDao
) {
    suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> {
        return programDao.getSelectedChannelPrograms(ids = channelsIds)
            .asProgramFromEntities()
            .filter { program ->
                program.dateTimeEnd > System.currentTimeMillis()
            }
    }

    suspend fun loadProgramsForChannel(channelId: String): List<Program> {
        return programDao.getSingleChannelPrograms(channelId = channelId)
            .asProgramFromEntities()
            .filter { program ->
                program.dateTimeEnd > System.currentTimeMillis()
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
        var entities = emptyList<ProgramEntity>()
        try {
            val ch = epgService.getChannelProgram(channelId).chPrograms
            entities = ch.asProgramEntities(channelId = channelId)
        } catch (ex: Exception) {
            // todo extract and refactor
            val noData = mutableListOf<ProgramEntity>()
            var multip = 0
            (1..5).forEach { _ ->
                val start = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS * multip
                val end = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS * (multip + 2)
                val p = ProgramEntity(
                    dateTimeStart = start,
                    dateTimeEnd = end,
                    title = "No Data",
                    channelId = channelId
                )
                noData.add(p)
                multip += 2
            }
            entities = noData
        } finally {
            if (entities.count() > COUNT_ZERO) {
                programDao.apply {
                    deletePrograms(channelId = channelId)
                    insertPrograms(channels = entities)
                }
            }
        }
    }
}
