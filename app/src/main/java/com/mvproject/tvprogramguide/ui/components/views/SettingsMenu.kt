package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    title: String = NO_VALUE_STRING,
    onAction: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable {
                onAction()
            }
            .padding(
                horizontal = MaterialTheme.dimens.size12,
                vertical = MaterialTheme.dimens.size8
            )
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

        Divider(
            color = MaterialTheme.colorScheme.onSurface,
            thickness = MaterialTheme.dimens.size1
        )
    }
}

@Preview
@Composable
fun SettingsMenuView() {
    TvGuideTheme {
        SettingsMenu(title = "TetstTitle")
    }
}

@Preview
@Composable
fun SettingsMenuDarkView() {
    TvGuideTheme(true) {
        SettingsMenu(title = "TetstTitle")
    }
}