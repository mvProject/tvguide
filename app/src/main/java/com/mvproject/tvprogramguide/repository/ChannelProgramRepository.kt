package com.mvproject.tvprogramguide.repository

import android.text.format.DateUtils
import androidx.annotation.WorkerThread
import com.mvproject.tvprogramguide.database.ProgramDao
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.utils.Mappers.asProgramEntities
import com.mvproject.tvprogramguide.utils.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.utils.Mappers.asProgramsFromJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ChannelProgramRepository @Inject constructor(
    private val epgService: EpgService,
    private val programDao: ProgramDao
) {
    init {
        Timber.d("Injection ChannelProgramRepository")
    }

    @WorkerThread
    fun loadChannelProgram(channelId: String) = flow {
        val databasePrograms = programDao.getSelectedChannelPrograms(channelId)
        if (databasePrograms.isEmpty()) {
            val networkPrograms = epgService.getChannelProgram(channelId).ch_programme
            programDao.insertPrograms(networkPrograms.asProgramEntities(channelId))
            emit(networkPrograms.asProgramsFromJson())
        } else {
            emit(databasePrograms.asProgramFromEntities())
        }
    }.flowOn(Dispatchers.IO)


    suspend fun loadChannelsProgram(channels: List<String>): List<Program>{
        val actualDate = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS
        val networkPrograms = mutableListOf<Program>()
        channels.forEach { id ->
            val ch = epgService.getChannelProgram(id).ch_programme
            programDao.insertPrograms(ch.asProgramEntities(id).filter { it.dateTime > actualDate })
            networkPrograms.addAll(ch.asProgramsFromJson(id).filter { it.dateTime > actualDate })
        }
        return networkPrograms
    }

    fun loadChannelsProgramFlow(channels: List<String>) = flow{
        val actualDate = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS
        val networkPrograms = mutableListOf<Program>()
        channels.forEach { id ->
            val ch = epgService.getChannelProgram(id).ch_programme
            programDao.insertPrograms(ch.asProgramEntities(id).filter { it.dateTime > actualDate })
            networkPrograms.addAll(ch.asProgramsFromJson(id).filter { it.dateTime > actualDate })
        }
        emit(networkPrograms)
    }
}