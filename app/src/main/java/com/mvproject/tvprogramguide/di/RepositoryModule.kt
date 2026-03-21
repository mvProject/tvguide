package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.BackupRepository
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IBackupRepository
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IAllChannelRepository> { AllChannelRepository(get()) }
    single<IChannelListRepository> { ChannelListRepository(get()) }
    single<IProgramRepository> { ProgramRepository(get()) }
    single<IPreferenceRepository> { PreferenceRepository(get()) }
    single<ISelectedChannelRepository> { SelectedChannelRepository(get()) }
    single<IBackupRepository> { BackupRepository(backupDataSource = get(), auth = get()) }
}
