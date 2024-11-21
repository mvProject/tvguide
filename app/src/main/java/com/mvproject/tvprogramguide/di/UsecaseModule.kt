package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.domain.usecases.AddChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.CleanProgramsUseCase
import com.mvproject.tvprogramguide.domain.usecases.DeleteChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.GetAvailableChannels
import com.mvproject.tvprogramguide.domain.usecases.GetProgramsByChannel
import com.mvproject.tvprogramguide.domain.usecases.GetSelectedChannels
import com.mvproject.tvprogramguide.domain.usecases.SaveChannelsSelection
import com.mvproject.tvprogramguide.domain.usecases.SelectChannelListUseCase
import com.mvproject.tvprogramguide.domain.usecases.SelectedChannelsWithPrograms
import com.mvproject.tvprogramguide.domain.usecases.ToggleProgramSchedule
import com.mvproject.tvprogramguide.domain.usecases.UpdateChannelsInfoUseCase
import com.mvproject.tvprogramguide.domain.usecases.UpdateProgramsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::AddChannelListUseCase)
    singleOf(::CleanProgramsUseCase)
    singleOf(::DeleteChannelListUseCase)
    singleOf(::GetAvailableChannels)
    singleOf(::GetProgramsByChannel)
    singleOf(::GetSelectedChannels)
    singleOf(::SaveChannelsSelection)
    singleOf(::SelectChannelListUseCase)
    singleOf(::SelectedChannelsWithPrograms)
    singleOf(::ToggleProgramSchedule)
    singleOf(::UpdateChannelsInfoUseCase)
    singleOf(::UpdateProgramsUseCase)
}
