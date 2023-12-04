package com.mvproject.tvprogramguide.ui.main.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.navigation.NavigationHost
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermissionState = rememberPermissionState(
                    Manifest.permission.POST_NOTIFICATIONS
                )

                val isNotificationGranted = notificationPermissionState.status.isGranted
                Timber.w("testing notificationPermissionState permission $isNotificationGranted")
                if (!isNotificationGranted) {
                    LaunchedEffect(key1 = notificationPermissionState) {
                        notificationPermissionState.launchPermissionRequest()
                    }

                }
            }

            val isDarkTheme = rememberIsDarkTheme()

            TvGuideTheme(isDarkTheme) {
                updateTheme(
                    darkTheme = isDarkTheme,
                    targetColor = MaterialTheme.colorScheme.inverseOnSurface.toArgb()
                )

                val navController = rememberNavController()
                val screen by viewModel.startDestination

                if (screen.isNotEmpty()) {
                    NavigationHost(
                        navController = navController,
                        startScreen = screen
                    )
                }
            }
        }
    }

    private fun updateTheme(darkTheme: Boolean, targetColor: Int) {
        window.apply {
            statusBarColor = targetColor
            navigationBarColor = targetColor
            WindowInsetsControllerCompat(this, this.decorView)
                .isAppearanceLightStatusBars = !darkTheme
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
