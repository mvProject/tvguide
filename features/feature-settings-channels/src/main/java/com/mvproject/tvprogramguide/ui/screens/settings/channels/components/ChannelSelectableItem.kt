package com.mvproject.tvprogramguide.ui.screens.settings.channels.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil3.compose.AsyncImage
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_900

@Composable
fun ChannelSelectableItem(
    modifier: Modifier = Modifier,
    channelName: String,
    channelLogo: String,
    fallbackPainter: Painter,
    isSelected: Boolean = true,
    isDragged: Boolean = false,
    onClickAction: () -> Unit,
) {
    val transition = updateTransition(targetState = isSelected, label = "transition")

    val iconColor by transition.animateColor(label = "textColor") { state ->
        if (state) {
            MaterialTheme.colorScheme.inverseOnSurface
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    }

    val iconColorBackground by transition.animateColor(label = "iconColorBackground") { state ->
        if (state) {
            MaterialTheme.colorScheme.outline
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    }

    val colorBackground by animateColorAsState(
        if (isDragged) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec =
            tween(
                durationMillis = ANIM_DURATION_900,
                easing = LinearOutSlowInEasing,
            ),
        label = "colorBackground",
    )

    ListItem(
        modifier = modifier.clip(MaterialTheme.shapes.extraSmall),
        colors =
            ListItemDefaults.colors(
                containerColor = colorBackground,
            ),
        leadingContent = {
            AsyncImage(
                model = channelLogo,
                placeholder = fallbackPainter,
                error = fallbackPainter,
                contentDescription = channelName,
                modifier =
                    Modifier
                        .size(MaterialTheme.dimens.size38)
                        .background(
                            color =
                                MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.extraSmall,
                        ),
            )
        },
        headlineContent = {
            Text(
                text = channelName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        trailingContent = {
            FilledIconButton(
                onClick = onClickAction,
                colors =
                    IconButtonDefaults.filledIconButtonColors(
                        containerColor = iconColorBackground,
                        contentColor = iconColor,
                    ),
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Outlined.Delete else Icons.Outlined.Add,
                    contentDescription = "Action",
                )
            }
        },
    )
}

@Composable
@PreviewLightDark
fun ChannelSelectableItemPreview() {
    TvGuideTheme {
        Column {
            ChannelSelectableItem(
                channelName = "Channel1",
                channelLogo = "Logo1",
                fallbackPainter = ColorPainter(Color.Gray),
                isSelected = true,
                onClickAction = {},
            )
            ChannelSelectableItem(
                channelName = "Channel2",
                channelLogo = "Logo2",
                fallbackPainter = ColorPainter(Color.Gray),
                isSelected = false,
                onClickAction = {},
            )
        }
    }
}
