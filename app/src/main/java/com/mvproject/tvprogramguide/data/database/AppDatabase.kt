package com.mvproject.tvprogramguide.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.database.dao.UserChannelsListDao
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.data.model.entity.UserChannelsListEntity

@Database(
    entities = [
        AvailableChannelEntity::class,
        ProgramEntity::class,
        SelectedChannelEntity::class,
        UserChannelsListEntity::class
    ],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun allChannelDao(): AllChannelDao

    abstract fun programDao(): ProgramDao

    abstract fun selectedChannelDao(): SelectedChannelDao

    abstract fun userChannelsListDao(): UserChannelsListDao
}
