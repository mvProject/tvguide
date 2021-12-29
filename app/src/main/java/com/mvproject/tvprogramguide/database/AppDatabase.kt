package com.mvproject.tvprogramguide.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvproject.tvprogramguide.model.entity.ChannelEntity
import com.mvproject.tvprogramguide.model.entity.CustomListEntity
import com.mvproject.tvprogramguide.model.entity.ProgramEntity
import com.mvproject.tvprogramguide.model.entity.SelectedChannelEntity

@Database(entities = [
    ChannelEntity::class,
    ProgramEntity::class,
    SelectedChannelEntity::class,
    CustomListEntity::class],
    version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun allChannelDao(): AllChannelDao

    abstract fun programDao(): ProgramDao

    abstract fun selectedChannelDao(): SelectedChannelDao

    abstract fun customListDao(): CustomListDao
}