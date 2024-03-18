package com.mvproject.tvprogramguide.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mvproject.tvprogramguide.ui.screens.onboard.navigation.onboardScreen
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.navigation.navigateToSelectedChannels
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.navigation.selectedChannelsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.navigateToSettingsApp
import com.mvproject.tvprogramguide.ui.screens.settings.app.navigation.settingsAppScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.navigateToSettingsChannel
import com.mvproject.tvprogramguide.ui.screens.settings.channels.navigation.settingsChannelScreen
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.navigateToSettingsGeneral
import com.mvproject.tvprogramguide.ui.screens.settings.general.navigation.settingsGeneralScreen
import com.mvproject.tvprogramguide.ui.screens.singlechannel.navigation.navigateToSingleChannel
import com.mvproject.tvprogramguide.ui.screens.singlechannel.navigation.singleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.navigation.navigateToUserCustomList
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.navigation.userCustomListScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startScreen: String,
) {
    NavHost(
        navController = navController,
        startDestination = startScreen,
        modifier =
            Modifier
                .systemBarsPadding(),
    ) {
        onboardScreen(
            onComplete = navController::navigateToSelectedChannels,
        )

        selectedChannelsScreen(
            onNavigateSingleChannel = navController::navigateToSingleChannel,
            onNavigateSettings = navController::navigateToSettingsGeneral,
            onNavigateChannelsList = navController::navigateToUserCustomList,
        )

        singleChannelScreen(
            onNavigateBack = navController::popBackStack,
        )

        settingsGeneralScreen(
            onNavigateBack = navController::popBackStack,
            onNavigateAppSettings = navController::navigateToSettingsApp,
            onNavigateChannelSettings = navController::navigateToUserCustomList,
        )

        settingsAppScreen(
            onNavigateBack = navController::popBackStack,
        )

        userCustomListScreen(
            onNavigateBack = navController::popBackStack,
            onNavigateItem = navController::navigateToSettingsChannel,
        )

        settingsChannelScreen()
    }
}
