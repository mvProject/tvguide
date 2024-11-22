package com.mvproject.tvprogramguide.di

import androidx.work.WorkManager
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    single { WorkManager.getInstance(get()) }
    worker { FullUpdateProgramsWorker(get(), get()) }
}
