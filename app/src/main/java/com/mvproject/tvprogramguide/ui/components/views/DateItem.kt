package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme

@Composable
fun DateItem(date: String) {
    ListItem(
        modifier = Modifier.clip(MaterialTheme.shapes.extraSmall),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                leadingIconColor = MaterialTheme.colorScheme.tertiary,
            ),
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
            )
        },
        headlineContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        },
    )
}

@PreviewLightDark
@Composable
fun DateItemPreview() {
    TvGuideTheme {
        DateItem("22.03.2012")
    }
}
