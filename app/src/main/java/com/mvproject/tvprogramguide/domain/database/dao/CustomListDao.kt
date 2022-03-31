package com.mvproject.tvprogramguide.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tvprogramguide.data.entity.CustomListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomList(channelList: CustomListEntity)

    @Query("SELECT * FROM custom_list WHERE id = :id")
    suspend fun getSingleCustomList(id: Int): CustomListEntity

    @Query("SELECT * FROM custom_list")
    fun getAllCustomLists(): Flow<List<CustomListEntity>>

    @Query("DELETE FROM custom_list")
    suspend fun deleteAllCustomLists()

    @Query("DELETE FROM custom_list WHERE id = :id")
    suspend fun deleteSingleCustomList(id: Int)
}
