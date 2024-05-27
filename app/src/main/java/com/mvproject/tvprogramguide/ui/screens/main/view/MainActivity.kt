package com.mvproject.tvprogramguide.ui.screens.main.view

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
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

    private lateinit var appUpdateManager: AppUpdateManager

    private val listener =
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate()
            }
        }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != RESULT_OK) {
                Timber.e("testing Update flow failed! Result code: " + result.resultCode)
                // If the update is canceled or fails,
                // you can request to start the update again.
            }
        }

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
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        appUpdateManager.registerListener(listener)

        checkForAppUpdates()

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
                    screen?.let { route ->
                        NavigationHost(
                            navController = navController,
                            startScreen = route,
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.isImmediateUpdateAllowed) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    )
                }
            }
            if (appUpdateInfo.isFlexibleUpdateAllowed) {
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager.completeUpdate()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
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

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                )
            }
        }
    }
}
