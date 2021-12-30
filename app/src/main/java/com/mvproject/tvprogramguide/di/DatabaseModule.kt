package com.mvproject.tvprogramguide.di

import android.app.Application
import androidx.room.Room
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.database.*
import com.mvproject.tvprogramguide.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.database.dao.CustomListDao
import com.mvproject.tvprogramguide.database.dao.ProgramDao
import com.mvproject.tvprogramguide.database.dao.SelectedChannelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                application.getString(R.string.database)
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAllChannelDao(appDatabase: AppDatabase): AllChannelDao {
        return appDatabase.allChannelDao()
    }

    @Provides
    @Singleton
    fun provideProgramDao(appDatabase: AppDatabase): ProgramDao {
        return appDatabase.programDao()
    }

    @Provides
    @Singleton
    fun provideSelectedChannelDao(appDatabase: AppDatabase): SelectedChannelDao {
        return appDatabase.selectedChannelDao()
    }

    @Provides
    @Singleton
    fun provideCustomListDao(appDatabase: AppDatabase): CustomListDao {
        return appDatabase.customListDao()
    }
}