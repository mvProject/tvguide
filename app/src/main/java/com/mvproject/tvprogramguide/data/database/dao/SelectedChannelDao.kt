package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.*
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelWithIconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: SelectedChannelEntity)

    @Update
    suspend fun updateChannels(channelsForUpdate: List<SelectedChannelEntity>)

    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    suspend fun getSelectedChannels(listName: String): List<SelectedChannelWithIconEntity>

    @Query("SELECT channel_id FROM selected_channels")
    suspend fun getSelectedChannelsIds(): List<String>

    @Query("SELECT channel_name FROM selected_channels WHERE channel_id = :id")
    suspend fun getChannelNameById(id: String): String

    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    fun getSelectedChannelsFlow(listName: String): Flow<List<SelectedChannelEntity>>

    @Query("DELETE FROM selected_channels WHERE channel_id = :channelId")
    suspend fun deleteSelectedChannel(channelId: String)
}
