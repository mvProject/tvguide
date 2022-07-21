package com.mvproject.tvprogramguide.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.ui.selectedchannels.view.ChannelScreen
import com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel.ChannelViewModel
import com.mvproject.tvprogramguide.ui.settings.SettingsOptions
import com.mvproject.tvprogramguide.ui.settings.app.SettingsScreen
import com.mvproject.tvprogramguide.ui.settings.channels.view.TabMainScreen
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.CustomListViewModel
import com.mvproject.tvprogramguide.ui.singlechannel.view.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.singlechannel.viewmodel.SingleChannelViewModel
import com.mvproject.tvprogramguide.ui.usercustomlist.view.UserCustomListScreen
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_200
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_600
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_900
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun NavigationHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppRoutes.Channels.route,
    ) {
        composable(
            AppRoutes.Channels.route,
            enterTransition = { scaleIn(animationSpec = tween(ANIM_DURATION_600)) },
            exitTransition = {
                scaleOut(animationSpec = tween(ANIM_DURATION_200)) +
                        fadeOut(animationSpec = tween(ANIM_DURATION_200))
            },
        ) {
            val channelViewModel = hiltViewModel<ChannelViewModel>()
            ChannelScreen(
                viewModel = channelViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(
            AppRoutes.SelectedChannel.route,
            enterTransition = { scaleIn(animationSpec = tween(ANIM_DURATION_600)) },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION_200)) +
                        fadeOut(animationSpec = tween(ANIM_DURATION_200))
            }
        ) { backStackEntry ->
            val singleChannelProgramsViewModel = hiltViewModel<SingleChannelViewModel>()
            val channelId =
                backStackEntry.arguments?.getString(ARGUMENT_CHANNEL_ID) ?: NO_VALUE_STRING
            val channelName =
                backStackEntry.arguments?.getString(ARGUMENT_CHANNEL_NAME) ?: NO_VALUE_STRING
            SingleChannelScreen(
                viewModel = singleChannelProgramsViewModel,
                channelId = channelId,
                channelName = channelName,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            AppRoutes.OptionSettings.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIM_DURATION_900)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIM_DURATION_900)) }
        ) {
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

        composable(
            AppRoutes.AppSettings.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIM_DURATION_900)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIM_DURATION_900)) }
        ) {
            SettingsScreen {
                navController.popBackStack()
            }
        }

        composable(
            AppRoutes.UserCustomList.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIM_DURATION_900)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIM_DURATION_900)) }
        ) {
            val userCustomListViewModel = hiltViewModel<UserCustomListViewModel>()
            UserCustomListScreen(
                viewModel = userCustomListViewModel,
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

        composable(
            AppRoutes.ChannelSettings.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIM_DURATION_900)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIM_DURATION_900)) }
        ) { backStackEntry ->
            val customListViewModel = hiltViewModel<CustomListViewModel>()
            val userListName =
                backStackEntry.arguments?.getString(ARGUMENT_USER_LIST_NAME) ?: NO_VALUE_STRING
            TabMainScreen(
                viewModel = customListViewModel,
                userListName = userListName
            )
        }
    }
}
