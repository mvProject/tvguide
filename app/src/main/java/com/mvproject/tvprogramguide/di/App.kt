package com.mvproject.tvprogramguide.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper.Companion.PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper.Companion.PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker.Companion.UPDATE_NOTIFICATION_CHANNEL_ID
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker.Companion.UPDATE_NOTIFICATION_CHANNEL_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import timber.log.Timber

class App :
    Application(), ImageLoaderFactory, KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
        }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val updateChannel = NotificationChannel(
                UPDATE_NOTIFICATION_CHANNEL_ID,
                UPDATE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val schedulingChannel = NotificationChannel(
                PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_ID,
                PROGRAM_SCHEDULED_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(updateChannel)
            notificationManager.createNotificationChannel(schedulingChannel)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            // .logger(DebugLogger())
            .build()
    }
}
