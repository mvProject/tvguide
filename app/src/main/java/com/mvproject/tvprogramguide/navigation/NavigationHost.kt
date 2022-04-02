package com.mvproject.tvprogramguide.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.ui.selectedchannels.view.ChannelScreen
import com.mvproject.tvprogramguide.ui.settings.channels.view.TabMainScreen
import com.mvproject.tvprogramguide.ui.singlechannel.view.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.settings.SettingsOptions
import com.mvproject.tvprogramguide.ui.settings.app.SettingsScreen
import com.mvproject.tvprogramguide.ui.usercustomlist.view.UserCustomListScreen
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.utils.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.utils.NavConstants.ARGUMENT_USER_LIST_NAME

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NavigationHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Channels.route,
    ) {
        composable(AppRoutes.Channels.route) {
            ChannelScreen(
                onSettingsClick = {
                    navController.navigate(AppRoutes.OptionSettings.route)
                },
                onChannelClick = { channel ->
                    navController.navigate(
                        AppRoutes.SelectedChannel.applyArgs(
                            channelId = channel.channelId,
                            channelName = channel.channelName
                        )
                    )
                }
            )
        }

        composable(AppRoutes.SelectedChannel.route) { backStackEntry ->
            val channelId =
                backStackEntry.arguments?.getString(ARGUMENT_CHANNEL_ID) ?: NO_VALUE_STRING
            val channelName =
                backStackEntry.arguments?.getString(ARGUMENT_CHANNEL_NAME) ?: NO_VALUE_STRING
            SingleChannelScreen(
                channelId = channelId,
                channelName = channelName,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.OptionSettings.route) {
            SettingsOptions(
                onBackClick = {
                    navController.popBackStack()
                },
                onAppSettings = {
                    navController.navigate(AppRoutes.AppSettings.route)
                },
                onChannelSettings = {
                    navController.navigate(AppRoutes.UserCustomList.route)
                }
            )
        }

        composable(AppRoutes.AppSettings.route) {
            SettingsScreen {
                navController.popBackStack()
            }
        }

        composable(AppRoutes.UserCustomList.route) {
            UserCustomListScreen(
                onItemClick = { id ->
                    navController.navigate(
                        AppRoutes.ChannelSettings.applyArgs(
                            userListName = id,
                        )
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ChannelSettings.route) { backStackEntry ->
            val userListName =
                backStackEntry.arguments?.getString(ARGUMENT_USER_LIST_NAME) ?: NO_VALUE_STRING
            TabMainScreen(userListName = userListName)
        }
    }
}