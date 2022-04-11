package com.mvproject.tvprogramguide.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun SettingsOptions(
    onBackClick: () -> Unit = {},
    onChannelSettings: () -> Unit = {},
    onAppSettings: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {

        ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
            onBackClick()
        }

        Spacer(modifier = Modifier.padding(vertical = MaterialTheme.dimens.size16))

        Text(
            text = stringResource(id = R.string.settings_channels_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size8)
                .clickable {
                    onChannelSettings()
                },
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.appTypography.textSemiBold,
            fontSize = MaterialTheme.dimens.font16
        )

        Divider(
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = MaterialTheme.dimens.size8)
        )

        Spacer(modifier = Modifier.padding(vertical = MaterialTheme.dimens.size8))

        Text(
            text = stringResource(id = R.string.settings_app_settings_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size8)
                .clickable {
                    onAppSettings()
                },
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.appTypography.textSemiBold,
            fontSize = MaterialTheme.dimens.font16
        )

        Divider(
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = MaterialTheme.dimens.size8)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsOptionsViewDark() {
    TvGuideTheme(true) {
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
