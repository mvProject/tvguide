package com.mvproject.tvprogramguide.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App :
    Application(),
    Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(
            object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String =
                    String.format(
                        "%s:%s",
                        element.methodName,
                        super.createStackElementTag(element),
                    )
            },
        )
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()
}
