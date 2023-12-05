package com.mvproject.tvprogramguide.ui.screens.settings.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun SettingsOptions(
    onBackClick: () -> Unit = {},
    onChannelSettings: () -> Unit = {},
    onAppSettings: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.settings_title)
            ) {
                onBackClick()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.settings_channels_settings_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8)
                    .clickable {
                        onChannelSettings()
                    },
                style = MaterialTheme.typography.bodyMedium,
            )

            Divider(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.size6,
                    vertical = MaterialTheme.dimens.size1
                ),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = MaterialTheme.dimens.size1
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

            Text(
                text = stringResource(id = R.string.settings_app_settings_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8)
                    .clickable {
                        onAppSettings()
                    },
                style = MaterialTheme.typography.bodyMedium,
            )

            Divider(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.size6,
                    vertical = MaterialTheme.dimens.size1
                ),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = MaterialTheme.dimens.size1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsOptionsViewDark() {
    TvGuideTheme(darkTheme = true) {
        SettingsOptions()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsOptionsView() {
    TvGuideTheme() {
        SettingsOptions()
    }
}
