package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.data.network.NetworkClient.createHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        createHttpClient()
    }
}
