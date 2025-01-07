package com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.backup.SettingsBackupScreen
import com.mvproject.tvprogramguide.ui.screens.settings.backup.SettingsBackupViewModel
import com.mvproject.tvprogramguide.utils.AppConstants
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSettingsBackup() {
    if (canNavigate) {
        this.navigate(AppRoutes.SettingsBackup)
    }
}

fun NavGraphBuilder.settingsBackupScreen(
    onNavigateBack: () -> Unit
) {
    composable<AppRoutes.SettingsBackup>(
        enterTransition = {
            fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
    ) {
        val viewModel = koinViewModel<SettingsBackupViewModel>()
        SettingsBackupScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}