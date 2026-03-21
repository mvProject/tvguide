package com.mvproject.tvprogramguide.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.mvproject.tvprogramguide.BuildConfig
import com.mvproject.tvprogramguide.data.datasource.FirebaseBackupDataSource
import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.domain.contract.IBackupDataSource
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<IProgramDataSource> { ProgramDataSource(get()) }
    single<FirebaseDatabase> { Firebase.database(BuildConfig.FIREBASE_DATABASE_URL) }
    single<FirebaseAuth> { Firebase.auth }
    single<IBackupDataSource> {
        FirebaseBackupDataSource(
            database = get(),
            databaseTable = BuildConfig.FIREBASE_DATABASE_TABLE,
        )
    }
}
