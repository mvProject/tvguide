package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asProgramFromEntities
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.response.ProgramResponse2
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.utils.TimeUtils.correctTimeZone
import kotlinx.datetime.Clock
import javax.inject.Inject

class ChannelProgramRepository
    @Inject
    constructor(
        private val epgService: EpgService,
        private val programDao: ProgramDao,
    ) {
        suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> =
            programDao
                .getSelectedChannelPrograms(ids = channelsIds)
                //   .map { it.correctTimeZone() }
                .asProgramFromEntities()
                .filter { program ->
                    program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
                }

        suspend fun loadProgramsForChannel(channelId: String): List<Program> =
            programDao
                .getSingleChannelPrograms(channelId = channelId)
                //   .map { it.correctTimeZone() }
                .asProgramFromEntities()
                .filter { program ->
                    program.dateTimeEnd > Clock.System.now().toEpochMilliseconds()
                }

        suspend fun loadProgramsCount(channelsIds: List<String>): Int =
            programDao
                .getSelectedChannelPrograms(ids = channelsIds)
                .groupBy { program ->
                    program.channelId
                }.keys
                .count()

    /*    suspend fun loadProgram2(channelId: String) {
            val parsedTable =
                loadElements(
                    sourceUrl = "https://epg.ott-play.com/php/show_prog.php?f=edem/epg/$channelId.json",
                )

            val programs =
                buildList {
                    parsedTable.forEach { element ->
                        val (date, title, description) = element.parseElementDataAsProgram()

                        val (start, end) = parseDateTime(input = date)

                        val programEntity =
                            ProgramEntity(
                                dateTimeStart = start,
                                dateTimeEnd = end,
                                title = title,
                                description = description,
                                channelId = channelId,
                            )
                        add(programEntity)
                    }
                }
            Timber.d("testing loadProgram2 for $channelId programs ${programs.count()}")
            if (programs.isNotEmpty()) {
                programDao.apply {
                    deletePrograms(channelId = channelId)
                    insertPrograms(channels = programs.map { it.correctTimeZone() })
                }
            }
        }

        @Transaction
        suspend fun loadProgram(channelId: String) {
            val entities =
                try {
                    val prg = epgService.getChannelProgram(channelId)
                    val ch = prg.chPrograms
                    if (ch.isEmpty()) {
                        emptyList()
                    } else {
                        ch.asProgramEntities(channelId = channelId)
                    }
                } catch (ex: Exception) {
                    Timber.e("testing loadProgram for $channelId IllegalStateException  ${ex.localizedMessage}")
                    emptyList()
                }
            if (entities.isNotEmpty()) {
                programDao.apply {
                    deletePrograms(channelId = channelId)
                    insertPrograms(channels = entities.map { it.correctTimeZone() })
                }
            }
        }*/

        @Transaction
        suspend fun refreshPrograms(
            channelId: String,
            programs: List<ProgramResponse2>,
        ) {
            val entities = programs.map { item -> item.asProgramEntity(id = channelId) }

            programDao.apply {
                deletePrograms(channelId = channelId)
                insertPrograms(channels = entities.map { it.correctTimeZone() })
            }
        }

        suspend fun updateProgram(program: Program) {
            programDao.updateProgram(programForUpdate = program.toEntity())
        }
    }
