package com.mvproject.tvprogramguide.ui.screens.usercustomlist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.ChannelListScreen
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.ChannelListViewModel
import com.mvproject.tvprogramguide.utils.AppConstants

fun NavController.navigateToChannelList() {
    if (canNavigate) {
        this.navigate(AppRoutes.UserCustomList)
    }
}

fun NavGraphBuilder.channelListScreen(
    onNavigateBack: () -> Unit,
    onNavigateItem: (String) -> Unit,
) {
    composable<AppRoutes.UserCustomList>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(AppConstants.ANIM_DURATION_600),
            ) + fadeIn(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(AppConstants.ANIM_DURATION_600),
            ) + fadeOut(animationSpec = tween(AppConstants.ANIM_DURATION_600))
        },
    ) {
        val channelListViewModel = hiltViewModel<ChannelListViewModel>()

        ChannelListScreen(
            viewModel = channelListViewModel,
            onNavigateItem = onNavigateItem,
            onNavigateBack = onNavigateBack,
        )
    }
}
