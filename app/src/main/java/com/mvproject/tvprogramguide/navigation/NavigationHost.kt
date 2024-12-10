package com.mvproject.tvprogramguide.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mvproject.tvprogramguide.ui.screens.channellist.navigation.channelListScreen
import com.mvproject.tvprogramguide.ui.screens.channellist.navigation.navigateToChannelList
import com.mvproject.tvprogramguide.ui.screens.channels.selected.navigation.selectedChannelsScreen
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.navigateToSingleChannel
import com.mvproject.tvprogramguide.ui.screens.channels.single.navigation.singleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.navigateToSettingsApp
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.settingsAppScreen
import com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation.navigateToSettingsBackup
import com.mvproject.tvprogramguide.ui.screens.settings.backup.navigation.settingsBackupScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.navigateToSettingsChannel
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.settingsChannelScreen
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.navigateToSettingsGeneral
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.settingsGeneralScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startScreen: AppRoutes,
) {
    NavHost(
        navController = navController,
        startDestination = startScreen,
    ) {
        selectedChannelsScreen(
            onNavigateSingleChannel = navController::navigateToSingleChannel,
            onNavigateSettings = navController::navigateToSettingsGeneral,
            onNavigateChannelsList = navController::navigateToChannelList,
        )

        singleChannelScreen(
            onNavigateBack = navController::navigateToBack,
        )

        settingsGeneralScreen(
            onNavigateBack = navController::navigateToBack,
            onNavigateAppSettings = navController::navigateToSettingsApp,
            onNavigateChannelSettings = navController::navigateToChannelList,
            onNavigateBackupSettings = navController::navigateToSettingsBackup,
        )

        settingsAppScreen(
            onNavigateBack = navController::navigateToBack,
        )

        channelListScreen(
            onNavigateBack = navController::navigateToBack,
            onNavigateItem = navController::navigateToSettingsChannel,
        )

        settingsChannelScreen(
            onNavigateBack = navController::navigateToBack,
        )

        settingsBackupScreen(
            onNavigateBack = navController::navigateToBack,
        )
    }
}

val NavController.canNavigate: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavController.navigateToBack() {
    if (canNavigate) {
        navigateUp()
    }
}
