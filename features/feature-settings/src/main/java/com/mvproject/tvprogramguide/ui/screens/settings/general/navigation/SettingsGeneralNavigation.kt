package com.mvproject.tvprogramguide.ui.screens.settings.general.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.general.SettingsGeneralScreen
import com.mvproject.tvprogramguide.utils.navEnterTransition
import com.mvproject.tvprogramguide.utils.navExitTransition
import com.mvproject.tvprogramguide.utils.navPopEnterTransition
import com.mvproject.tvprogramguide.utils.navPopExitTransition

fun NavController.navigateToSettingsGeneral() {
    if (canNavigate) {
        this.navigate(AppRoutes.SettingsGeneral)
    }
}

fun NavGraphBuilder.settingsGeneralScreen(
    onNavigateBack: () -> Unit,
    onNavigateAppSettings: () -> Unit,
    onNavigateChannelSettings: () -> Unit,
    onNavigateBackupSettings: () -> Unit,
) {
    composable<AppRoutes.SettingsGeneral>(
        enterTransition = { navEnterTransition },
        exitTransition = { navExitTransition },
        popEnterTransition = { navPopEnterTransition },
        popExitTransition = { navPopExitTransition },
    ) {
        SettingsGeneralScreen(
            onNavigateBack = onNavigateBack,
            onNavigateAppSettings = onNavigateAppSettings,
            onNavigateChannelSettings = onNavigateChannelSettings,
            onNavigateBackupSettings = onNavigateBackupSettings
        )
    }
}
