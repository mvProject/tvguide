package com.mvproject.tvprogramguide.ui.components.views

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
import coil.compose.AsyncImage
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun ChannelItem(
    channelName: String,
    channelLogo: String,
    onClickAction: () -> Unit,
) {
    ListItem(
        modifier =
            Modifier
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
                modifier = Modifier.size(MaterialTheme.dimens.size38),
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

@PreviewLightDark
@Composable
fun ChannelItemPreview() {
    TvGuideTheme {
        ChannelItem(
            channelName = "TV1000 Comedy",
            channelLogo = "https://picon.ml/vip-comedy.png",
            onClickAction = {},
        )
    }
}
