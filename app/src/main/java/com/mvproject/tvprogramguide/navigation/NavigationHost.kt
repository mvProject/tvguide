package com.mvproject.tvprogramguide.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mvproject.tvprogramguide.BuildConfig
import com.mvproject.tvprogramguide.ui.screens.channellist.navigation.channelListScreen
import com.mvproject.tvprogramguide.ui.screens.channellist.navigation.navigateToChannelList
import com.mvproject.tvprogramguide.ui.screens.channels.selected.navigation.selectedChannelsScreen
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.navigateToSingleChannel
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.singleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.onboard.navigation.onBoardScreen
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.navigateToSettingsApp
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.settingsAppScreen
import com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation.navigateToSettingsBackup
import com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation.settingsBackupScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.navigateToSettingsChannel
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.settingsChannelScreen
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.navigateToSettingsGeneral
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.settingsGeneralScreen
import com.mvproject.tvprogramguide.utils.navEnterTransition
import com.mvproject.tvprogramguide.utils.navExitTransition
import com.mvproject.tvprogramguide.utils.navPopEnterTransition
import com.mvproject.tvprogramguide.utils.navPopExitTransition

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationHost(
    navController: NavHostController,
    startScreen: AppRoutes,
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startScreen,
            enterTransition = { navEnterTransition },
            exitTransition = { navExitTransition },
            popEnterTransition = { navPopEnterTransition },
            popExitTransition = { navPopExitTransition },
        ) {
            onBoardScreen(
                onComplete = {
                    navController.navigate(AppRoutes.Channels) {
                        popUpTo(AppRoutes.OnBoard) { inclusive = true }
                    }
                }
            )

            selectedChannelsScreen(
                sharedTransitionScope = this@SharedTransitionLayout,
                onNavigateSingleChannel = navController::navigateToSingleChannel,
                onNavigateSettings = navController::navigateToSettingsGeneral,
                onNavigateChannelsList = navController::navigateToChannelList,
            )

            singleChannelScreen(
                sharedTransitionScope = this@SharedTransitionLayout,
                onNavigateBack = navController::navigateUp,
            )

            settingsGeneralScreen(
                onNavigateBack = navController::navigateUp,
                onNavigateAppSettings = navController::navigateToSettingsApp,
                onNavigateChannelSettings = navController::navigateToChannelList,
                onNavigateBackupSettings = navController::navigateToSettingsBackup,
            )

            settingsAppScreen(
                onNavigateBack = navController::navigateUp,
            )

            channelListScreen(
                sharedTransitionScope = this@SharedTransitionLayout,
                onNavigateBack = navController::navigateUp,
                onNavigateItem = navController::navigateToSettingsChannel,
            )

            settingsChannelScreen(
                sharedTransitionScope = this@SharedTransitionLayout,
                onNavigateBack = navController::navigateUp,
            )

            settingsBackupScreen(
                webClientId = BuildConfig.WEB_CLIENT_ID,
                onNavigateBack = navController::navigateUp,
            )
        }
    }
}

