package com.mvproject.tvprogramguide.ui.components.views

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil3.compose.AsyncImage
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.closeControlAnimation
import com.mvproject.tvprogramguide.utils.openControlAnimation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelItem(
    channelName: String,
    channelLogo: String,
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
                    enter = openControlAnimation,
                    exit = closeControlAnimation,
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
                    placeholder = painterResource(R.drawable.no_channel_logo),
                    error = painterResource(R.drawable.no_channel_logo),
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
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    onClickAction = {},
                )
            }
        }
    }
}
