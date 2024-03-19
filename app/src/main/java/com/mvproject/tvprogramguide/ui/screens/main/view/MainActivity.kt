package com.mvproject.tvprogramguide.ui.screens.main.view

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.navigation.NavigationHost
import com.mvproject.tvprogramguide.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ),
        )
        super.onCreate(savedInstanceState)

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermissionState =
                    rememberPermissionState(
                        Manifest.permission.POST_NOTIFICATIONS,
                    )

                val isNotificationGranted = notificationPermissionState.status.isGranted
                Timber.i("notificationPermissionState permission $isNotificationGranted")
                if (!isNotificationGranted) {
                    LaunchedEffect(key1 = notificationPermissionState) {
                        notificationPermissionState.launchPermissionRequest()
                    }
                }
            }

            val isDarkTheme = rememberIsDarkTheme()

            windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme

            TvGuideTheme(
                darkTheme = isDarkTheme,
            ) {
                val navController = rememberNavController()
                val screen by viewModel.startDestination
                Box(modifier = Modifier) {
                    if (screen.isNotEmpty()) {
                        NavigationHost(
                            navController = navController,
                            startScreen = screen,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun rememberIsDarkTheme(): Boolean {
        val isSystemDarkTheme = isSystemInDarkTheme()

        val theme by remember(viewModel) {
            viewModel.currentTheme
        }.collectAsState(initial = AppThemeOptions.SYSTEM)

        val isDarkTheme =
            when (theme) {
                AppThemeOptions.LIGHT -> false
                AppThemeOptions.DARK -> true
                AppThemeOptions.SYSTEM -> isSystemDarkTheme
            }
        return isDarkTheme
    }
}
