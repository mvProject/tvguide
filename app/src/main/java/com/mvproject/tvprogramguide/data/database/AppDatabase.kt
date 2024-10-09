package com.mvproject.tvprogramguide.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import com.mvproject.tvprogramguide.data.database.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.database.entity.ChannelsListEntity
import com.mvproject.tvprogramguide.data.database.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.database.entity.SelectedChannelEntity

@Database(
    entities = [
        AvailableChannelEntity::class,
        ProgramEntity::class,
        SelectedChannelEntity::class,
        ChannelsListEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allChannelDao(): AllChannelDao

    abstract fun programDao(): ProgramDao

    abstract fun selectedChannelDao(): SelectedChannelDao

    abstract fun userChannelsListDao(): ChannelsListDao
}
