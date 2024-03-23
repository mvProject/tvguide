package com.mvproject.tvprogramguide.ui.screens.settings.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.SettingsMenu
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun SettingsGeneralScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateChannelSettings: () -> Unit = {},
    onNavigateAppSettings: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.settings_title),
                onBackClick = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(vertical = MaterialTheme.dimens.size8),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
        ) {
            SettingsMenu(
                title = stringResource(id = R.string.settings_channels_settings_title),
                onAction = onNavigateChannelSettings,
            )

            SettingsMenu(
                title = stringResource(id = R.string.settings_app_settings_title),
                onAction = onNavigateAppSettings,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun SettingsOptionsViewDark() {
    TvGuideTheme {
        SettingsGeneralScreen()
    }
}
