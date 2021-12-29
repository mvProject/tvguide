package com.mvproject.tvprogramguide.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.model.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AllChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannelList(channels: List<ChannelEntity>)

    @Query("SELECT * FROM all_channels WHERE channel_id = :id")
    suspend fun getChannel(id: String): ChannelEntity?

    @Query("SELECT * FROM all_channels")
    suspend fun getChannelList(): List<ChannelEntity>
}
