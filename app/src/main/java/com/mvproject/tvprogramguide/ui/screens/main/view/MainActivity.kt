package com.mvproject.tvprogramguide.ui.screens.main.view

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
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
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
            setOnExitAnimationListener { splashScreen ->
                // to remove splashscreen with no animation
                // splashScreen.remove()
                /*                ObjectAnimator.ofFloat(
                                    splashScreen.view,
                                    "scaleX",
                                    0.5f,
                                    0f,
                                ).apply {
                                    interpolator = OvershootInterpolator()
                                    duration = 300
                                    doOnEnd { splashScreen.remove() }
                                    start()
                                }
                                ObjectAnimator.ofFloat(
                                    splashScreen.view,
                                    "scaleY",
                                    0.5f,
                                    0f,
                                ).apply {
                                    interpolator = OvershootInterpolator()
                                    duration = 300
                                    doOnEnd { splashScreen.remove() }
                                    start()
                                }*/

                ObjectAnimator.ofFloat(
                    splashScreen.view,
                    View.TRANSLATION_Y,
                    // from top to down
                    0f,
                    splashScreen.view.height.toFloat(),
                ).apply {
                    // deceleration interpolaror, duration
                    interpolator = DecelerateInterpolator()
                    duration = 500L
                    // do not forget to remove the splash screen
                    doOnEnd { splashScreen.remove() }
                    start()
                }
            }
        }

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

            TvGuideTheme(isDarkTheme) {
                updateTheme(
                    darkTheme = isDarkTheme,
                    targetColor = MaterialTheme.colorScheme.inverseOnSurface.toArgb(),
                )

                val navController = rememberNavController()
                val screen by viewModel.startDestination

                if (screen.isNotEmpty()) {
                    NavigationHost(
                        navController = navController,
                        startScreen = screen,
                    )
                }
            }
        }
    }

    private fun updateTheme(
        darkTheme: Boolean,
        targetColor: Int,
    ) {
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

        val isDarkTheme =
            when (theme) {
                AppThemeOptions.LIGHT -> false
                AppThemeOptions.DARK -> true
                AppThemeOptions.SYSTEM -> isSystemDarkTheme
            }
        return isDarkTheme
    }
}
