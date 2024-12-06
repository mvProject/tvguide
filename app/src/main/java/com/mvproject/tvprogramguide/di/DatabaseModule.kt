package com.mvproject.tvprogramguide.di

import androidx.room.Room
import com.mvproject.tvprogramguide.data.database.AppDatabase
import com.mvproject.tvprogramguide.data.database.DbConstants.DATABASE
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(
                get(),
                AppDatabase::class.java,
                DATABASE
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<AllChannelDao> { get<AppDatabase>().allChannelDao() }
    single<ProgramDao> { get<AppDatabase>().programDao() }
    single<SelectedChannelDao> { get<AppDatabase>().selectedChannelDao() }
    single<ChannelsListDao> { get<AppDatabase>().userChannelsListDao() }
}