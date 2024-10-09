package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelsListDao {
    @Upsert
    suspend fun addUserChannelsList(channelList: ChannelsListEntity)

    @Upsert
    suspend fun addUserChannelsLists(channelLists: List<ChannelsListEntity>)

    @Query("SELECT * FROM channelsList")
    fun getAllUserChannelsListsAsFlow(): Flow<List<ChannelsListEntity>>

    @Query("SELECT * FROM channelsList")
    suspend fun getAllUserChannelsLists(): List<ChannelsListEntity>

    @Query("DELETE FROM channelsList WHERE id = :channelId")
    suspend fun deleteSingleUserChannelsList(channelId: Int)
}
