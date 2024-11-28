package com.mvproject.tvprogramguide.di

import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelViewModel
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelViewModel
import com.mvproject.tvprogramguide.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.backup.SettingsBackupViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.channels.ChannelSettingsViewModel
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.ChannelListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::ChannelViewModel)
    viewModelOf(::SingleChannelViewModel)
    viewModelOf(::ChannelListViewModel)
    viewModelOf(::AppSettingsViewModel)
    viewModelOf(::ChannelSettingsViewModel)
    viewModelOf(::SettingsBackupViewModel)
}
