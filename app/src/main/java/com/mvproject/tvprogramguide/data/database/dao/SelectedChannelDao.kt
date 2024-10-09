package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelWithIconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: SelectedChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channel: List<SelectedChannelEntity>)

    @Update
    suspend fun updateChannels(channelsForUpdate: List<SelectedChannelEntity>)

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    suspend fun getSelectedChannels(listName: String): List<SelectedChannelWithIconEntity>

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList IN (SELECT name FROM channelsList WHERE isSelected == 1)")
    fun getChannelsForCurrentListAsFlow(): Flow<List<SelectedChannelWithIconEntity>>

    @Query("SELECT title FROM selected_channels WHERE id = :id")
    suspend fun getChannelNameById(id: String): String

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    fun getSelectedChannelsFlow(listName: String): Flow<List<SelectedChannelWithIconEntity>>

    @Query("DELETE FROM selected_channels WHERE id = :channelId")
    suspend fun deleteSelectedChannel(channelId: String)

    @Query("DELETE FROM selected_channels WHERE parentList = :list")
    suspend fun deleteChannels(list: String)
}
