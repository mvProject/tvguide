package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.mvproject.tvprogramguide.utils.closeControlAnimation
import com.mvproject.tvprogramguide.utils.openControlAnimation

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelListItem(
    modifier: Modifier = Modifier,
    listName: String,
    isSelected: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemSelect: () -> Unit = {},
    onItemAction: () -> Unit = {},
    onDeleteAction: () -> Unit = {},
) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.outline

    with(sharedTransitionScope) {
        ListItem(
            modifier = modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = listName),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = openControlAnimation,
                    exit = closeControlAnimation,
                )
                .combinedClickable(
                    onClick = onItemAction, onLongClick = onItemSelect
                )
                .border(
                    width = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .clip(MaterialTheme.shapes.extraSmall),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            headlineContent = {
                Text(
                    text = listName, style = if (isSelected) MaterialTheme.typography.titleLarge
                    else MaterialTheme.typography.titleMedium, color = contentColor
                )
            },
            trailingContent = {
                IconButton(
                    onClick = onDeleteAction,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = contentColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = Icons.Outlined.Delete.name
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@PreviewLightDark
private fun ChannelListItemPreview() {
    TvGuideTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                Column {
                    ChannelListItem(
                        listName = "Channel1",
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                    )
                    ChannelListItem(
                        listName = "Channel2",
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                    )
                }
            }
        }
    }
}
