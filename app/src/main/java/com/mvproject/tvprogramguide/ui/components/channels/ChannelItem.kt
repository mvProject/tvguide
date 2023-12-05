package com.mvproject.tvprogramguide.ui.components.channels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.forwardingPainter

@Composable
fun ChannelItem(
    channelName: String,
    channelLogo: String,
    onClickAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClickAction()
            },
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(MaterialTheme.dimens.size8)

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
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                ),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size38)
                    .clip(MaterialTheme.shapes.small)
                // .background(
                //    color = MaterialTheme.colorScheme.onPrimary
                //        .copy(alpha = MaterialTheme.dimens.alpha20)
                // ),
            )

            Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

            Text(
                text = channelName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChannelItemViewDark() {
    TvGuideTheme(true) {
        ChannelItem(
            channelName = "TV1000 Comedy",
            channelLogo = "https://picon.ml/vip-comedy.png",
            onClickAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChannelItemView() {
    TvGuideTheme() {
        ChannelItem(
            channelName = "TV1000 Comedy",
            channelLogo = "https://picon.ml/vip-comedy.png",
            onClickAction = {}
        )
    }
}
