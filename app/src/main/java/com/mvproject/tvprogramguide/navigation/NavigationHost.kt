package com.mvproject.tvprogramguide.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardScreen
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardViewModel
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.ChannelScreen
import com.mvproject.tvprogramguide.ui.screens.selectedchannels.ChannelViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.app.AppSettingsViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.app.SettingsScreen
import com.mvproject.tvprogramguide.ui.screens.settings.channels.CustomListViewModel
import com.mvproject.tvprogramguide.ui.screens.settings.channels.TabMainScreen
import com.mvproject.tvprogramguide.ui.screens.settings.general.SettingsOptions
import com.mvproject.tvprogramguide.ui.screens.singlechannel.view.SingleChannelScreen
import com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel.SingleChannelViewModel
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.UserCustomListScreen
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.UserCustomListViewModel
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_600
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NavigationHost(navController: NavHostController, startScreen: String) {
    NavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = Modifier
            .systemBarsPadding()
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
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_600)
                ) + fadeIn(animationSpec = tween(ANIM_DURATION_600))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
