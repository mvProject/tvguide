package com.mvproject.tvprogramguide.ui.screens.channels.single.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.channels.single.SingleChannelViewModel
import com.mvproject.tvprogramguide.utils.AppConstants
import timber.log.Timber

fun NavController.navigateToSingleChannel(
    channelId: String,
    channelName: String,
) {
    if (canNavigate) {
        this.navigate(AppRoutes.SingleChannel(channelId = channelId, channelName = channelName))
    }
}

internal class SingleChannelArgs(val channelId: String, val channelName: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        channelId = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_ID]) as String,
        channelName = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_NAME]) as String,
    )
}

fun NavGraphBuilder.singleChannelScreen(onNavigateBack: () -> Unit) {
    composable<AppRoutes.SingleChannel>(
        enterTransition = {
            fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
    ) { backstackEntry ->
        val channel = backstackEntry.toRoute<AppRoutes.SingleChannel>()
        Timber.d("testing singleChannelScreen channelId ${channel.channelId}")
        Timber.d("testing singleChannelScreen channelName ${channel.channelName}")
        val singleChannelViewModel = hiltViewModel<SingleChannelViewModel>()

        SingleChannelScreen(
            viewModel = singleChannelViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
