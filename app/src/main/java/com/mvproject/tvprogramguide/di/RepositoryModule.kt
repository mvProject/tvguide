package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.BackupRepository
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AllChannelRepository)
    singleOf(::ChannelListRepository)
    singleOf(::ProgramRepository)
    singleOf(::PreferenceRepository)
    singleOf(::SelectedChannelRepository)
    singleOf(::BackupRepository)
}
