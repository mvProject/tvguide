package com.mvproject.tvprogramguide.infrastructure.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import org.koin.dsl.module

data class FirebaseConfig(
    val databaseUrl: String,
    val databaseTable: String,
)

fun firebaseModule(config: FirebaseConfig) = module {
    single<FirebaseDatabase> { Firebase.database(config.databaseUrl) }
    single<FirebaseAuth> { Firebase.auth }
    single<IProgramDataSource> { ProgramDataSource(get()) }
}
