package com.mvproject.tvprogramguide.ui.screens.onboard.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardScreen
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardViewModel

fun NavGraphBuilder.onboardScreen(onComplete: () -> Unit) {
    composable<AppRoutes.OnBoard> {
        val onBoardViewModel = hiltViewModel<OnBoardViewModel>()

        OnBoardScreen(
            viewModel = onBoardViewModel,
            onComplete = onComplete,
        )
    }
}
