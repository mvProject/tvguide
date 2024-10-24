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

/**
 * Repository class for managing TV program operations.
 *
 * @property programDao Data Access Object for program operations.
 */
class ProgramRepository
@Inject
constructor(
    private val programDao: ProgramDao,
) {
    /**
     * Loads programs for multiple channels starting from the current date.
     *
     * @param channelsIds List of channel IDs to fetch programs for.
     * @return List of Program objects for the specified channels.
     */
    suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program> {
        return programDao
            .getSelectedChannelPrograms(
                timeStamp = TimeUtils.actualDate,
                selectedIds = channelsIds
            )
            .asProgramFromEntities()
    }

    /**
     * Loads programs for a single channel starting from the current date.
     *
     * @param channelId ID of the channel to fetch programs for.
     * @return List of Program objects for the specified channel.
     */
    suspend fun loadProgramsForChannel(channelId: String): List<Program> {
        return programDao
            .getChannelProgramsById(
                timeStamp = TimeUtils.actualDate,
                channelId = channelId
            )
            .asProgramFromEntities()
    }

    /**
     * Updates programs for a specific channel. This operation is performed as a transaction
     * to ensure data consistency.
     *
     * @param channelId ID of the channel to update programs for.
     * @param programs List of ProgramDTO objects containing the new program data.
     */
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

    /**
     * Removes all programs with a start date earlier than the specified date.
     *
     * @param date The cutoff date in milliseconds. Programs before this date will be deleted.
     */
    suspend fun cleanProgramsBeforeDate(date: Long) {
        programDao.deleteProgramsByDate(timeStamp = date)
    }

    /**
     * Updates a single program in the database.
     *
     * @param program The Program object to be updated.
     */
    suspend fun updateProgram(program: Program) {
        programDao.updateProgram(programForUpdate = program.toEntity())
    }
}
