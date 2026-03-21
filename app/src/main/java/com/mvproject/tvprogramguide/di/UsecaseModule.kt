package com.mvproject.tvprogramguide.di

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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::AddChannelListUseCase)
    singleOf(::CleanProgramsUseCase)
    singleOf(::DeleteChannelListUseCase)
    singleOf(::GetAvailableChannelsUseCase)
    singleOf(::GetProgramsByChannelUseCase)
    singleOf(::GetSelectedChannelsUseCase)
    singleOf(::SelectChannelListUseCase)
    singleOf(::GetSelectedChannelsWithProgramsUseCase)
    singleOf(::ToggleProgramScheduleUseCase)
    singleOf(::UpdateChannelsInfoUseCase)
    singleOf(::UpdateProgramsUseCase)
    singleOf(::BackupCreateUseCase)
    singleOf(::BackupRestoreUseCase)
}
