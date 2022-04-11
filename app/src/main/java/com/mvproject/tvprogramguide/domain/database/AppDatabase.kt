package com.mvproject.tvprogramguide.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvproject.tvprogramguide.data.entity.ChannelEntity
import com.mvproject.tvprogramguide.data.entity.CustomListEntity
import com.mvproject.tvprogramguide.data.entity.ProgramEntity
import com.mvproject.tvprogramguide.data.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.domain.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.domain.database.dao.CustomListDao
import com.mvproject.tvprogramguide.domain.database.dao.ProgramDao
import com.mvproject.tvprogramguide.domain.database.dao.SelectedChannelDao

@Database(
    entities = [
        ChannelEntity::class,
        ProgramEntity::class,
        SelectedChannelEntity::class,
        CustomListEntity::class
    ],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun allChannelDao(): AllChannelDao

    abstract fun programDao(): ProgramDao

    abstract fun selectedChannelDao(): SelectedChannelDao

    abstract fun customListDao(): CustomListDao
}
