package com.mvproject.tvprogramguide.infrastructure.di

import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.BackupCreateUseCase
import com.mvproject.tvprogramguide.domain.usecases.BackupRestoreUseCase
import com.mvproject.tvprogramguide.domain.usecases.CleanProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetAvailableChannelsUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannelUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannelsWithProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramScheduleUseCase
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.domain.usecases.UpdateProgramsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::AddChannelListUseCase)
    factoryOf(::CleanProgramsUseCase)
    factoryOf(::DeleteChannelListUseCase)
    factoryOf(::GetAvailableChannelsUseCase)
    factoryOf(::GetProgramsByChannelUseCase)
    factoryOf(::GetSelectedChannelsUseCase)
    factoryOf(::SelectChannelListUseCase)
    factoryOf(::GetSelectedChannelsWithProgramsUseCase)
    factoryOf(::ToggleProgramScheduleUseCase)
    factoryOf(::UpdateChannelsInfoUseCase)
    factoryOf(::UpdateProgramsUseCase)
    factoryOf(::BackupCreateUseCase)
    factoryOf(::BackupRestoreUseCase)
}
