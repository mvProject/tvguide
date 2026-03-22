package com.mvproject.tvprogramguide.ui.screens.channels.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil3.compose.AsyncImage
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.containerTransformBoundsTransform
import com.mvproject.tvprogramguide.utils.sharedBoundsEnter
import com.mvproject.tvprogramguide.utils.sharedBoundsExit

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelItem(
    channelName: String,
    channelLogo: String,
    fallbackPainter: Painter,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClickAction: () -> Unit,
) {
    with(sharedTransitionScope) {
        ListItem(
            modifier =
                Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = channelName),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = sharedBoundsEnter,
                        exit = sharedBoundsExit,
                        boundsTransform = containerTransformBoundsTransform,
                    )
                    .clickable(onClick = onClickAction)
                    .clip(MaterialTheme.shapes.extraSmall),
            colors =
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            leadingContent = {
                AsyncImage(
                    model = channelLogo,
                    placeholder = fallbackPainter,
                    error = fallbackPainter,
                    contentDescription = channelName,
                    modifier = Modifier.size(MaterialTheme.dimens.size38)
                )
            },
            headlineContent = {
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.titleMedium,
                )
            },
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@Composable
fun ChannelItemPreview() {
    TvGuideTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                ChannelItem(
                    channelName = "TV1000 Comedy",
                    channelLogo = "https://picon.ml/vip-comedy.png",
                    fallbackPainter = ColorPainter(Color.Gray),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    onClickAction = {},
                )
            }
        }
    }
}
