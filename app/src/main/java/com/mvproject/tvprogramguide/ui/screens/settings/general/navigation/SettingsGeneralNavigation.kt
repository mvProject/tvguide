package com.mvproject.tvprogramguide.ui.screens.settings.general.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.general.SettingsGeneralScreen
import com.mvproject.tvprogramguide.utils.AppConstants

fun NavController.navigateToSettingsGeneral() {
    if (canNavigate) {
        this.navigate(AppRoutes.SettingsGeneral.route)
    }
}

fun NavGraphBuilder.settingsGeneralScreen(
    onNavigateBack: () -> Unit,
    onNavigateAppSettings: () -> Unit,
    onNavigateChannelSettings: () -> Unit,
) {
    composable(
        AppRoutes.SettingsGeneral.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(AppConstants.ANIM_DURATION_600),
            ) + fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(AppConstants.ANIM_DURATION_600),
            ) + fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
    ) {
        SettingsGeneralScreen(
            onNavigateBack = onNavigateBack,
            onNavigateAppSettings = onNavigateAppSettings,
            onNavigateChannelSettings = onNavigateChannelSettings,
        )
    }
}
