package com.mvproject.tvprogramguide.infrastructure.di

import com.mvproject.tvprogramguide.data.datasource.FirebaseBackupDataSource
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.BackupRepository
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.ProgramRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IAllChannelRepository
import com.mvproject.tvprogramguide.domain.contract.IBackupDataSource
import com.mvproject.tvprogramguide.domain.contract.IBackupRepository
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IProgramRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import org.koin.dsl.module

fun dataModule(firebaseConfig: FirebaseConfig) = module {
    single<IAllChannelRepository> { AllChannelRepository(get()) }
    single<IChannelListRepository> { ChannelListRepository(get()) }
    single<IProgramRepository> { ProgramRepository(get()) }
    single<ISelectedChannelRepository> { SelectedChannelRepository(get()) }
    single<IBackupDataSource> {
        FirebaseBackupDataSource(
            database = get(),
            databaseTable = firebaseConfig.databaseTable
        )
    }
    single<IBackupRepository> {
        BackupRepository(backupDataSource = get(), auth = get())
    }
}
