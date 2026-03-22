package com.mvproject.tvprogramguide.infrastructure.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    firebaseConfig: FirebaseConfig,
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    appDeclaration()
    modules(
        datastoreModule,
        databaseModule,
        networkModule,
        firebaseModule(firebaseConfig),
        dataModule(firebaseConfig),
        useCaseModule,
        platformModule,
    )
}
