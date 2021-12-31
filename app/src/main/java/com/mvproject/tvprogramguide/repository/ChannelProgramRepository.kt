package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.database.dao.ProgramDao
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.utils.Mappers.asProgramEntities
import com.mvproject.tvprogramguide.utils.Mappers.asProgramFromEntities
import timber.log.Timber
import javax.inject.Inject

class ChannelProgramRepository @Inject constructor(
    private val epgService: EpgService,
    private val programDao: ProgramDao
) {
    init {
        Timber.d("Injection ChannelProgramRepository")
    }

    suspend fun loadChannelsProgram(channels: List<String>): List<Program> {
        val networkPrograms = mutableListOf<Program>()
        channels.forEach { id ->
            val programs = programDao.getSelectedChannelPrograms(id)
            if (programs.isEmpty()) {
                val ch = epgService.getChannelProgram(id).ch_programme
                val entities = ch.asProgramEntities(id)
                programDao.insertPrograms(entities)
                val models = entities.asProgramFromEntities(id)
                networkPrograms.addAll(models)
            } else {
                networkPrograms.addAll(
                    programs.asProgramFromEntities(id)
                )
            }
        }

        return networkPrograms
    }
}