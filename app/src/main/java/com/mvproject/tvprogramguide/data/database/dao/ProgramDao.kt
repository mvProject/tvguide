package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity

@Dao
interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(channels: List<ProgramEntity>)

    @Query("SELECT * FROM programs WHERE channelId = :channelId")
    suspend fun getSingleChannelPrograms(channelId: String): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE channelId IN (:ids)")
    suspend fun getSelectedChannelPrograms(ids: List<String>): List<ProgramEntity>

    @Query("DELETE FROM programs WHERE channelId = :channelId")
    suspend fun deletePrograms(channelId: String)

    @Update
    suspend fun updateProgram(programForUpdate: ProgramEntity)
}
