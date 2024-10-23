package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity

@Dao
interface ProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<ProgramEntity>)

    @Query("SELECT * FROM programs WHERE channelId = :channelId AND dateTimeEnd > :timeStamp")
    suspend fun getChannelProgramsById(timeStamp: Long, channelId: String): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE channelId IN (:selectedIds) AND dateTimeEnd > :timeStamp")
    suspend fun getSelectedChannelPrograms(timeStamp: Long, selectedIds: List<String>): List<ProgramEntity>

    @Query("DELETE FROM programs WHERE channelId = :channelId")
    suspend fun deletePrograms(channelId: String)

    @Query("DELETE FROM programs WHERE dateTimeEnd < :timeStamp")
    suspend fun deleteProgramsByDate(timeStamp: Long)

    @Upsert
    suspend fun updateProgram(programForUpdate: ProgramEntity)
}
