package com.mvproject.tvprogramguide.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
    exportSchema = true,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun allChannelDao(): AllChannelDao

    abstract fun programDao(): ProgramDao

    abstract fun selectedChannelDao(): SelectedChannelDao

    abstract fun userChannelsListDao(): ChannelsListDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX IF NOT EXISTS index_programs_channelId ON programs (channelId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_programs_dateTimeEnd ON programs (dateTimeEnd)")
            }
        }

        fun createDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
