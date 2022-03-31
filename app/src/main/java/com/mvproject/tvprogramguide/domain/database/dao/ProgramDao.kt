package com.mvproject.tvprogramguide.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.data.entity.ProgramEntity

@Dao
interface ProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(channels: List<ProgramEntity>)

    @Query("SELECT * FROM programs WHERE channelId = :id")
    suspend fun getSingleChannelPrograms(id: String): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE channelId IN (:ids)")
    suspend fun getSelectedChannelPrograms(ids: List<String>): List<ProgramEntity>

    @Query("SELECT * FROM programs")
    suspend fun getPrograms(): List<ProgramEntity>

    @Query("DELETE FROM programs WHERE channelId = :id")
    suspend fun deletePrograms(id: String)
}
