package com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.ui.screens.settings.channels.ChannelSettingsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.ChannelSettingsViewModel
import com.mvproject.tvprogramguide.utils.AppConstants

fun NavController.navigateToSettingsChannel(userListName: String) {
    this.navigate("${AppRoutes.ChannelSettings.route}/$userListName")
}

internal class SettingsChannelArgs(val userListName: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(userListName = checkNotNull(savedStateHandle[ARGUMENT_USER_LIST_NAME]) as String)
}

fun NavGraphBuilder.settingsChannelScreen() {
    composable(
        "${AppRoutes.ChannelSettings.route}/{$ARGUMENT_USER_LIST_NAME}",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(AppConstants.ANIM_DURATION_600)
            ) + fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(AppConstants.ANIM_DURATION_600)
            ) + fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        }
    ) {
        val channelSettingsViewModel = hiltViewModel<ChannelSettingsViewModel>()

        ChannelSettingsScreen(
            viewModel = channelSettingsViewModel
        )
    }
}