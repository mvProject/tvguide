package com.mvproject.tvprogramguide.ui.settings.channels.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants.ANIM_DURATION_900
import com.mvproject.tvprogramguide.domain.utils.forwardingPainter
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ChannelSelectableItem(
    channelName: String,
    channelLogo: String,
    isSelected: Boolean = true,
    isDragged: Boolean = false,
    onClickAction: () -> Unit
) {
    val colorBackground by animateColorAsState(
        if (isDragged) MaterialTheme.colorScheme.inverseOnSurface
            .copy(alpha = MaterialTheme.dimens.alpha30)
        else MaterialTheme.colorScheme.surface,
        animationSpec = tween(
            durationMillis = ANIM_DURATION_900,
            easing = LinearOutSlowInEasing
        )
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small),
        color = colorBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(channelLogo)
                    .crossfade(true)
                    .build(),
                placeholder = forwardingPainter(
                    painter = painterResource(R.drawable.no_channel_logo),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                ),
                error = forwardingPainter(
                    painter = painterResource(R.drawable.no_channel_logo),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                ),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size38)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
                    .background(
                        color = MaterialTheme.colorScheme.onSurface
                            .copy(alpha = MaterialTheme.dimens.alpha20)
                    )

            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Text(
                text = channelName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Icon(
                imageVector = if (isSelected) Icons.Outlined.Delete else Icons.Outlined.Add,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.tertiaryContainer,
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size24)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onClickAction()
                    }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChannelSelectableItemPreview() {
    TvGuideTheme {
        Column {
            ChannelSelectableItem(
                channelName = "Channel1",
                channelLogo = "Logo1",
                isSelected = true,
                onClickAction = {}
            )
            ChannelSelectableItem(
                channelName = "Channel2",
                channelLogo = "Logo2",
                onClickAction = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChannelSelectableItemPreviewDark() {
    TvGuideTheme(true) {
        Column {
            ChannelSelectableItem(
                channelName = "Channel1",
                channelLogo = "Logo1",
                isSelected = true,
                onClickAction = {}
            )
            ChannelSelectableItem(
                channelName = "Channel2",
                channelLogo = "Logo2",
                onClickAction = {}
            )
        }
    }
}
