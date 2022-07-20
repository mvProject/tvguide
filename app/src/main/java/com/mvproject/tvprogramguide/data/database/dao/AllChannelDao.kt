package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity

@Dao
interface AllChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannelList(availableChannels: List<AvailableChannelEntity>)

    @Query("SELECT * FROM all_channels")
    suspend fun getChannelList(): List<AvailableChannelEntity>

    @Query("DELETE FROM all_channels")
    suspend fun deleteChannels()
}
