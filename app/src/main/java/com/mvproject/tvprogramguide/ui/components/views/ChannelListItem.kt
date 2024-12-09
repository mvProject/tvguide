package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelListItem(
    modifier: Modifier = Modifier,
    listName: String,
    isSelected: Boolean = false,
    onItemSelect: () -> Unit = {},
    onItemAction: () -> Unit = {},
    onDeleteAction: () -> Unit = {},
) {
    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.onSurface

    ListItem(
        modifier =
        modifier
            .combinedClickable(
                onClick = onItemAction,
                onLongClick = onItemSelect
            )
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
                style = if (isSelected)
                    MaterialTheme.typography.titleLarge
                else
                    MaterialTheme.typography.titleMedium,
                color = contentColor
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
                    tint = contentColor
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
