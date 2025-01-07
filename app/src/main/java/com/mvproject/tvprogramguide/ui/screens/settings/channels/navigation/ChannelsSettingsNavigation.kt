package com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.channels.ChannelSettingsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.ChannelSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSettingsChannel(userListName: String) {
    if (canNavigate) {
        this.navigate(AppRoutes.ChannelSettings(userListName = userListName))
    }
}

internal class SettingsChannelArgs(val userListName: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(userListName = checkNotNull(savedStateHandle[ARGUMENT_USER_LIST_NAME]) as String)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.settingsChannelScreen(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateBack: () -> Unit
) {
    composable<AppRoutes.ChannelSettings> {
        val channelSettingsViewModel = koinViewModel<ChannelSettingsViewModel>()

        ChannelSettingsScreen(
            viewModel = channelSettingsViewModel,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onNavigateBack = onNavigateBack,
        )
    }
}
