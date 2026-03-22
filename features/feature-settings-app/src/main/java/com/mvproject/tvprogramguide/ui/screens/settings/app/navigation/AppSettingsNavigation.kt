package com.mvproject.tvprogramguide.ui.screens.settings.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsViewModel
import com.mvproject.tvprogramguide.utils.navEnterTransition
import com.mvproject.tvprogramguide.utils.navExitTransition
import com.mvproject.tvprogramguide.utils.navPopEnterTransition
import com.mvproject.tvprogramguide.utils.navPopExitTransition
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToSettingsApp() {
    if (canNavigate) {
        this.navigate(AppRoutes.AppSettings)
    }
}

fun NavGraphBuilder.settingsAppScreen(onNavigateBack: () -> Unit) {
    composable<AppRoutes.AppSettings>(
        enterTransition = { navEnterTransition },
        exitTransition = { navExitTransition },
        popEnterTransition = { navPopEnterTransition },
        popExitTransition = { navPopExitTransition },
    ) {
        val appSettingsViewModel = koinViewModel<AppSettingsViewModel>()

        AppSettingsScreen(
            viewModel = appSettingsViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
