package com.mvproject.tvprogramguide.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mvproject.tvprogramguide.data.database.DbConstants.DATABASE
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

    companion object {
        fun createDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE
            )
                // .addMigrations(*DatabaseMigrations)
                .build()
        }
    }
}
