package com.mvproject.tvprogramguide.ui.screens.onboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.navigation.canNavigate
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardScreen
import com.mvproject.tvprogramguide.ui.screens.onboard.OnBoardViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavController.navigateToOnBoard() {
    if (canNavigate) {
        this.navigate(AppRoutes.OnBoard)
    }
}

fun NavGraphBuilder.onBoardScreen(onComplete: () -> Unit) {
    composable<AppRoutes.OnBoard> {
        val viewModel = koinViewModel<OnBoardViewModel>()
        OnBoardScreen(viewModel = viewModel, onComplete = onComplete)
    }
}
