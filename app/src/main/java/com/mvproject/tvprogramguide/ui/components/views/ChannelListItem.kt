package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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

@Composable
fun ChannelListItem(
    modifier: Modifier = Modifier,
    listName: String,
    onItemAction: () -> Unit = {},
    onDeleteAction: () -> Unit = {},
) {
    ListItem(
        modifier =
            modifier
                .clickable(onClick = onItemAction)
                .border(
                    width = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .clip(MaterialTheme.shapes.extraSmall),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
        headlineContent = {
            Text(
                text = listName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        trailingContent = {
            FilledIconButton(
                onClick = onDeleteAction,
                colors =
                    IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                )
            }
        },
    )
}

@Composable
@PreviewLightDark
private fun ChannelListItemPreview() {
    TvGuideTheme {
        Column {
            ChannelListItem(
                listName = "Channel1",
            )
            ChannelListItem(
                listName = "Channel2",
            )
        }
    }
}
