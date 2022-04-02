package com.mvproject.tvprogramguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.navigation.NavigationHost
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            TvGuideTheme(isSystemInDarkTheme()) {
                NavigationHost(navController = navController)
            }
        }
    }
}