package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.database.dao.CustomListDao
import com.mvproject.tvprogramguide.database.dao.ProgramDao
import com.mvproject.tvprogramguide.netwotk.EpgService
import com.mvproject.tvprogramguide.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideAllChannelRepository(
        epgService: EpgService,
        channelDao: AllChannelDao
    ): AllChannelRepository {
        return AllChannelRepository(epgService, channelDao)
    }

    @Provides
    @ViewModelScoped
    fun provideChannelProgramRepository(
        epgService: EpgService,
        programDao: ProgramDao
    ): ChannelProgramRepository {
        return ChannelProgramRepository(epgService, programDao)
    }

    @Provides
    @ViewModelScoped
    fun provideChannelListRepository(
        customListDao: CustomListDao
    ): CustomListRepository {
        return CustomListRepository(customListDao)
    }
}