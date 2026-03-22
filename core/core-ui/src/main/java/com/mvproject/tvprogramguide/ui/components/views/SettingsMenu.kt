package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    title: String = String.empty,
    onAction: () -> Unit = {},
) {
    Column {
        ListItem(
            modifier =
                modifier
                    .clickable(onClick = onAction)
                    .clip(MaterialTheme.shapes.extraSmall),
            colors =
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
            headlineContent = {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface,
            thickness = MaterialTheme.dimens.size1,
        )
    }
}

@PreviewLightDark
@Composable
fun SettingsMenuView() {
    TvGuideTheme {
        SettingsMenu(title = "Test Title")
    }
}
