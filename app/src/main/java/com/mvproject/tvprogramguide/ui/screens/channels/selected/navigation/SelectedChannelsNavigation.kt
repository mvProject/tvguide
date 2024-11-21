package com.mvproject.tvprogramguide.ui.screens.channels.selected.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelScreen
import com.mvproject.tvprogramguide.ui.screens.channels.selected.ChannelViewModel
import com.mvproject.tvprogramguide.utils.AppConstants
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSelectedChannels() {
    if (canNavigate) {
        this.popBackStack()
        this.navigate(AppRoutes.Channels)
    }
}

fun NavGraphBuilder.selectedChannelsScreen(
    onNavigateSingleChannel: (String, String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateChannelsList: () -> Unit,
) {
    composable<AppRoutes.Channels>(
        enterTransition = {
            fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
    ) {
        val channelViewModel = koinViewModel<ChannelViewModel>()

        ChannelScreen(
            viewModel = channelViewModel,
            onNavigateSingleChannel = onNavigateSingleChannel,
            onNavigateSettings = onNavigateSettings,
            onNavigateChannelsList = onNavigateChannelsList,
        )
    }
}
