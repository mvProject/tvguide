package com.mvproject.tvprogramguide.ui.screens.settings.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsViewModel
import com.mvproject.tvprogramguide.utils.AppConstants

fun NavController.navigateToSettingsApp() {
    this.navigate(AppRoutes.AppSettings.route)
}

fun NavGraphBuilder.settingsAppScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        AppRoutes.AppSettings.route,
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
        val appSettingsViewModel = hiltViewModel<AppSettingsViewModel>()

        AppSettingsScreen(
            viewModel = appSettingsViewModel,
            onNavigateBack = onNavigateBack
        )
    }
}