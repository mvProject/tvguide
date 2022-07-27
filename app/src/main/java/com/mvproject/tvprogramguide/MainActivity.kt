package com.mvproject.tvprogramguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mvproject.tvprogramguide.navigation.NavigationHost
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.lightBlack
import com.mvproject.tvprogramguide.theme.midnightBlue
import com.mvproject.tvprogramguide.ui.settings.app.model.AppThemeOptions
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberAnimatedNavController()
            val isDarkTheme = rememberIsDarkTheme()
            updateTheme(isDarkTheme)

            TvGuideTheme(isDarkTheme) {
                NavigationHost(navController = navController)
            }
        }
    }

    private fun updateTheme(darkTheme: Boolean) {
        val targetColor = if (darkTheme) lightBlack.toArgb() else midnightBlue.toArgb()
        window.apply {
            statusBarColor = targetColor
            navigationBarColor = targetColor
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                !darkTheme
        }
    }

    @Composable
    private fun rememberIsDarkTheme(): Boolean {
        val isSystemDarkTheme = isSystemInDarkTheme()

        val theme by remember(viewModel) {
            viewModel.currentTheme
        }.collectAsState(initial = AppThemeOptions.SYSTEM)

        val isDarkTheme = when (theme) {
            AppThemeOptions.LIGHT -> false
            AppThemeOptions.DARK -> true
            AppThemeOptions.SYSTEM -> isSystemDarkTheme
        }
        return isDarkTheme
    }
}
