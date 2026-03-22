package com.mvproject.tvprogramguide.data.network

import com.mvproject.tvprogramguide.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkClient {

    const val EPG_FILE_PRIMARY = "https://iptvx.one/EPG_NOARCH.xml.gz"
    const val EPG_FILE2 = "http://epg.one/epg2.xml.gz"
    const val EPG_CHANNELS_URL = "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz"

    fun createHttpClient(): HttpClient {
        return HttpClient(OkHttp).config {
            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.ALL
                }
            }

            install(ContentEncoding) {
                gzip()
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 120_000
            }
        }
    }
}