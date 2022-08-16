package com.mvproject.tvprogramguide.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mvproject.tvprogramguide.data.utils.AppConstants.ANIM_DURATION_600
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.ui.onboard.view.OnBoardScreen
import com.mvproject.tvprogramguide.ui.onboard.viewmodel.OnBoardViewModel
import com.mvproject.tvprogramguide.ui.selectedchannels.view.ChannelScreen
import com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel.ChannelViewModel
import com.mvproject.tvprogramguide.ui.settings.SettingsOptions
import com.mvproject.tvprogramguide.ui.settings.app.view.SettingsScreen
import com.mvproject.tvprogramguide.ui.settings.app.viewmodel.AppSettingsViewModel
import com.mvproject.tvprogramguide.ui.settings.channels.view.TabMainScreen
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.CustomListViewModel
import com.mvproject.tvprogramguide.ui.singlechannel.view.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.singlechannel.viewmodel.SingleChannelViewModel
import com.mvproject.tvprogramguide.ui.usercustomlist.view.UserCustomListScreen
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun NavigationHost(navController: NavHostController, startScreen: String) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = Modifier
            .systemBarsPadding()
            .background(MaterialTheme.colors.primary)
    ) {
        composable(
            AppRoutes.OnBoard.route,
        ) {
            val onBoardViewModel = hiltViewModel<OnBoardViewModel>()
            OnBoardScreen(
                viewModel = onBoardViewModel,
                onComplete = {
                    navController.popBackStack()
                    navController.navigate(AppRoutes.Channels.route)
                }
            )
        }
        composable(
            AppRoutes.Channels.route,
            enterTransition = {
                fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION_600))
            }
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
            enterTransition = {
                fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION_600))
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
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeOut(animationSpec = tween(ANIM_DURATION_600))
            }
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
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeOut(animationSpec = tween(ANIM_DURATION_600))
            }
        ) {
            val appSettingsViewModel = hiltViewModel<AppSettingsViewModel>()
            SettingsScreen(
                viewModel = appSettingsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            AppRoutes.UserCustomList.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeOut(animationSpec = tween(ANIM_DURATION_600))
            }
        ) {
            val userCustomListViewModel = hiltViewModel<UserCustomListViewModel>()
            UserCustomListScreen(
                viewModel = userCustomListViewModel,
                onItemClick = { userListId ->
                    navController.navigate(
                        AppRoutes.ChannelSettings.applyArgs(
                            userListName = userListId,
                        )
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            AppRoutes.ChannelSettings.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeOut(animationSpec = tween(ANIM_DURATION_600))
            }
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
