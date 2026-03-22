package com.mvproject.tvprogramguide.infrastructure.di

import androidx.work.WorkManager
import com.mvproject.tvprogramguide.domain.contract.IProgramScheduler
import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import com.mvproject.tvprogramguide.domain.workers.FullUpdateProgramsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val platformModule = module {
    single { WorkManager.getInstance(get()) }
    singleOf(::NetworkHelper)
    singleOf(::NotificationHelper)
    single<IProgramScheduler> { ProgramSchedulerHelper(get()) }
    workerOf(::FullUpdateProgramsWorker)
}
