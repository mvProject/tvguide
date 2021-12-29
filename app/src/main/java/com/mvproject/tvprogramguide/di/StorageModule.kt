package com.mvproject.tvprogramguide.di

import android.content.Context
import com.mvproject.tvprogramguide.StoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun providesStoreManager(
        @ApplicationContext context: Context
    ) = StoreManager(context)
}
