package com.mvproject.tvprogramguide.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.database.entity.ChannelEntity
import com.mvproject.tvprogramguide.database.entity.SelectedChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: SelectedChannelEntity)

    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    suspend fun getSelectedChannels(listName: String): List<ChannelEntity>

    @Query("SELECT channel_id FROM selected_channels")
    suspend fun getSelectedChannelsIds(): List<String>

    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    fun getSelectedChannelsFlow(listName: String): Flow<List<ChannelEntity>>

    @Query("DELETE FROM selected_channels WHERE channel_id = :id")
    suspend fun deleteSelectedChannel(id: String)
}
