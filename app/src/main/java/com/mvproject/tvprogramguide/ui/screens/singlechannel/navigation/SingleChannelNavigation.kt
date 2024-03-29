package com.mvproject.tvprogramguide.ui.screens.singlechannel.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.ui.screens.singlechannel.view.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel.SingleChannelViewModel
import com.mvproject.tvprogramguide.utils.AppConstants

fun NavController.navigateToSingleChannel(channelId: String, channelName: String) {
    this.navigate("${AppRoutes.SingleChannel.route}/$channelId/$channelName")
}

internal class SingleChannelArgs(val channelId: String, val channelName: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        channelId = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_ID]) as String,
        channelName = checkNotNull(savedStateHandle[ARGUMENT_CHANNEL_NAME]) as String
    )
}

fun NavGraphBuilder.singleChannelScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        "${AppRoutes.SingleChannel.route}/{$ARGUMENT_CHANNEL_ID}/{$ARGUMENT_CHANNEL_NAME}",
        enterTransition = {
            fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        }
    ) {
        val singleChannelViewModel = hiltViewModel<SingleChannelViewModel>()

        SingleChannelScreen(
            viewModel = singleChannelViewModel,
            onNavigateBack = onNavigateBack
        )
    }
}