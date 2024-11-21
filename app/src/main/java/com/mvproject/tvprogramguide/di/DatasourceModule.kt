package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.data.datasource.ProgramDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::ProgramDataSource)
}
