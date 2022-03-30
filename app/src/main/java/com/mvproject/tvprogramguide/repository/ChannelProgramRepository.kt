package com.mvproject.tvprogramguide.repository

import android.text.format.DateUtils
import androidx.room.Transaction
import com.mvproject.tvprogramguide.database.dao.ProgramDao
import com.mvproject.tvprogramguide.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
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

    suspend fun loadChannelPrograms(channelsId: String): List<Program> {
        return programDao.getSingleChannelPrograms(channelsId)
            .asProgramFromEntities()
            .filter { it.dateTimeEnd > System.currentTimeMillis() }
    }

    suspend fun loadProgramsCount(channels: List<String>): Int {
        return programDao.getSelectedChannelPrograms(channels).groupBy { it.channelId }.keys.count()
    }

    @Transaction
    suspend fun loadProgram(id: String) {
        var entities = emptyList<ProgramEntity>()
        try {
            val ch = epgService.getChannelProgram(id).ch_programme
            entities = ch.asProgramEntities(id)

        } catch (ex: Exception) {
            val noData = mutableListOf<ProgramEntity>()
            var multip = 0
            (1..5).forEach { _ ->
                val start = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS * multip
                val end = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS * (multip + 2)
                val p = ProgramEntity(
                    dateTimeStart = start,
                    dateTimeEnd = end,
                    title = "No Data",
                    channelId = id
                )
                noData.add(p)
                multip += 2
            }
            entities = noData
        } finally {
            if (entities.count() > COUNT_ZERO) {
                programDao.apply {
                    deletePrograms(id)
                    insertPrograms(entities)
                }
            }
        }
    }
}