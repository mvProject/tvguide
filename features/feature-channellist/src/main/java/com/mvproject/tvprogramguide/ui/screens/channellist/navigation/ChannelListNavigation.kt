package com.mvproject.tvprogramguide.ui.screens.channellist.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.channellist.ChannelListScreen
import com.mvproject.tvprogramguide.ui.screens.channellist.ChannelListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToChannelList() {
    if (canNavigate) {
        this.navigate(AppRoutes.UserCustomList)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.channelListScreen(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateBack: () -> Unit,
    onNavigateItem: (String) -> Unit,
) {
    composable<AppRoutes.UserCustomList> {
        val channelListViewModel = koinViewModel<ChannelListViewModel>()

        ChannelListScreen(
            viewModel = channelListViewModel,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onNavigateItem = onNavigateItem,
            onNavigateBack = onNavigateBack,
        )
    }
}
