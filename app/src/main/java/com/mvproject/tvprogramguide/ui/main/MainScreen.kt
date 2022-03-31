package com.mvproject.tvprogramguide

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.main.MainViewModel

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()

    mainViewModel.checkAvailableChannelsUpdate()
    mainViewModel.checkFullProgramsUpdate()

    val navController = rememberNavController()

    TvGuideTheme {
        NavigationHost(navController = navController)
    }
}