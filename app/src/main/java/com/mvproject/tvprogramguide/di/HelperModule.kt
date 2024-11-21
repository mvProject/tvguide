package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.domain.helpers.NetworkHelper
import com.mvproject.tvprogramguide.domain.helpers.NotificationHelper
import com.mvproject.tvprogramguide.domain.helpers.ProgramSchedulerHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule = module {
    singleOf(::NetworkHelper)
    singleOf(::NotificationHelper)
    singleOf(::ProgramSchedulerHelper)
}
