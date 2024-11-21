package com.mvproject.tvprogramguide.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            datastoreModule,
            databaseModule,
            networkModule,
            repositoryModule,
            useCaseModule,
            helperModule,
            workerModule,
            dataSourceModule,
            viewModelsModule
        )
    }