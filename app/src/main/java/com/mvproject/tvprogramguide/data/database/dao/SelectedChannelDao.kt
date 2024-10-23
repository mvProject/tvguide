package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelWithIconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channel: List<SelectedChannelEntity>)

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList = :listName")
    suspend fun getSelectedChannels(listName: String): List<SelectedChannelWithIconEntity>

    @Transaction
    @Query("SELECT * FROM selected_channels WHERE parentList IN (SELECT name FROM channelsList WHERE isSelected == 1)")
    fun getChannelsForCurrentListAsFlow(): Flow<List<SelectedChannelWithIconEntity>>

    @Query("DELETE FROM selected_channels WHERE parentList = :list")
    suspend fun deleteChannels(list: String)
}
