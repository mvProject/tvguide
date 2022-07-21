package com.mvproject.tvprogramguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.data.model.entity.UserChannelsListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserChannelsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserChannelsList(channelList: UserChannelsListEntity)

    @Query("SELECT * FROM custom_list")
    fun getAllUserChannelsLists(): Flow<List<UserChannelsListEntity>>

    @Query("DELETE FROM custom_list WHERE id = :channelId")
    suspend fun deleteSingleUserChannelsList(channelId: Int)
}
