package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity

@Dao
interface ProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<ProgramEntity>)

    @Query("SELECT * FROM programs")
    suspend fun getChannelPrograms(): List<ProgramEntity>


    @Query("SELECT * FROM programs WHERE channelId = :channelId")
    suspend fun getChannelProgramsById(channelId: String): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE channelId IN (:selectedIds)")
    suspend fun getSelectedChannelPrograms(selectedIds: List<String>): List<ProgramEntity>

    @Query("DELETE FROM programs WHERE channelId = :channelId")
    suspend fun deletePrograms(channelId: String)

    @Query("DELETE FROM programs")
    suspend fun deleteAllPrograms()

    @Update
    suspend fun updateProgram(programForUpdate: ProgramEntity)
}
