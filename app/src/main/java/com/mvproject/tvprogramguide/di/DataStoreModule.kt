package com.mvproject.tvprogramguide.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

/*
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val TVGUIDE_PREFERENCES = "tvguide_preferences"

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(TVGUIDE_PREFERENCES) }
        )
    }
}
*/


val datastoreModule = module {
    single { providePreferencesDataStore(get()) }
}

fun providePreferencesDataStore(
    appContext: Context
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { appContext.preferencesDataStoreFile(TVGUIDE_PREFERENCES) }
    )
}

private const val TVGUIDE_PREFERENCES = "tvguide_preferences"