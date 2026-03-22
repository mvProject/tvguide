package com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.backup.SettingsBackupScreen
import com.mvproject.tvprogramguide.ui.screens.settings.backup.SettingsBackupViewModel
import com.mvproject.tvprogramguide.utils.navEnterTransition
import com.mvproject.tvprogramguide.utils.navExitTransition
import com.mvproject.tvprogramguide.utils.navPopEnterTransition
import com.mvproject.tvprogramguide.utils.navPopExitTransition
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
        enterTransition = { navEnterTransition },
        exitTransition = { navExitTransition },
        popEnterTransition = { navPopEnterTransition },
        popExitTransition = { navPopExitTransition },
    ) {
        val viewModel = koinViewModel<SettingsBackupViewModel>()
        SettingsBackupScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}