package com.mvproject.tvprogramguide.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.model.entity.ChannelEntity
import com.mvproject.tvprogramguide.model.entity.ProgramEntity

@Dao
interface ProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(channels: List<ProgramEntity>)

    @Query("SELECT * FROM programs WHERE channelId = :id")
    suspend fun getSelectedChannelPrograms(id: String): List<ProgramEntity>

    @Query("SELECT * FROM programs")
    suspend fun getPrograms(): List<ProgramEntity>
}
