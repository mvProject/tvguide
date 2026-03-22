package com.mvproject.tvprogramguide.infrastructure.di

import com.mvproject.tvprogramguide.data.database.AppDatabase
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.database.dao.ChannelsListDao
import com.mvproject.tvprogramguide.data.database.dao.ProgramDao
import com.mvproject.tvprogramguide.data.database.dao.SelectedChannelDao
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        AppDatabase.createDataBase(get())
    }

    single<AllChannelDao> { get<AppDatabase>().allChannelDao() }
    single<ProgramDao> { get<AppDatabase>().programDao() }
    single<SelectedChannelDao> { get<AppDatabase>().selectedChannelDao() }
    single<ChannelsListDao> { get<AppDatabase>().userChannelsListDao() }
}
