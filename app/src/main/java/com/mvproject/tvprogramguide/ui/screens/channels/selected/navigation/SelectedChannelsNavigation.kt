package com.mvproject.tvprogramguide.ui.screens.channels.selected.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelScreen
import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSelectedChannels() {
    if (canNavigate) {
        this.popBackStack()
        this.navigate(AppRoutes.Channels)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.selectedChannelsScreen(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateSingleChannel: (String, String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateChannelsList: () -> Unit,
) {
    composable<AppRoutes.Channels> {
        val channelViewModel = koinViewModel<ChannelViewModel>()

        ChannelScreen(
            viewModel = channelViewModel,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onNavigateSingleChannel = onNavigateSingleChannel,
            onNavigateSettings = onNavigateSettings,
            onNavigateChannelsList = onNavigateChannelsList,
        )
    }
}
