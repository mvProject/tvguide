package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: SelectedChannelEntity)

    @Update
    suspend fun updateChannels(channelsForUpdate: List<SelectedChannelEntity>)

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    suspend fun getSelectedChannels(listName: String): List<SelectedChannelWithIconEntity>

    @Query("SELECT channel_id FROM selected_channels")
    suspend fun getSelectedChannelsIds(): List<String>

    @Query("SELECT channel_name FROM selected_channels WHERE channel_id = :id")
    suspend fun getChannelNameById(id: String): String

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    fun getSelectedChannelsFlow(listName: String): Flow<List<SelectedChannelWithIconEntity>>

    @Query("DELETE FROM selected_channels WHERE channel_id = :channelId")
    suspend fun deleteSelectedChannel(channelId: String)
}
