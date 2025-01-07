package com.mvproject.tvprogramguide.ui.screens.channels.single.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSingleChannel(
    channelId: String,
    channelName: String,
) {
    if (canNavigate) {
        this.navigate(AppRoutes.SingleChannel(channelId = channelId, channelName = channelName))
    }
}

internal class SingleChannelArgs(
    val channelId: String,
    val channelName: String,
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        channelId = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_ID]) as String,
        channelName = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_NAME]) as String,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.singleChannelScreen(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateBack: () -> Unit
) {
    composable<AppRoutes.SingleChannel> {
        val singleChannelViewModel = koinViewModel<SingleChannelViewModel>()

        SingleChannelScreen(
            viewModel = singleChannelViewModel,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onNavigateBack = onNavigateBack,
        )
    }
}
