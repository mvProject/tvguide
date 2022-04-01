package com.mvproject.tvprogramguide.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.NavigationHost
import com.mvproject.tvprogramguide.theme.TvGuideTheme

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()

    mainViewModel.checkAvailableChannelsUpdate()
    mainViewModel.checkFullProgramsUpdate()

    val navController = rememberNavController()

    TvGuideTheme(isSystemInDarkTheme()) {
        NavigationHost(navController = navController)
    }
}