package com.mvproject.tvprogramguide.ui.selectedchannels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.mvproject.tvprogramguide.domain.utils.forwardingPainter
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

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
            .background(MaterialTheme.colors.surface)
            .clickable {
                onClickAction()
            },
        shape = RoundedCornerShape(MaterialTheme.dimens.size4),
        elevation = MaterialTheme.dimens.size2
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
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary)
                ),
                error = forwardingPainter(
                    painter = painterResource(R.drawable.no_channel_logo),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary)
                ),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size38)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
                    .background(
                        color = MaterialTheme.colors.onPrimary
                            .copy(alpha = MaterialTheme.dimens.alpha20)
                    ),
            )

            Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

            Text(
                text = channelName,
                fontSize = MaterialTheme.dimens.font16,
                style = MaterialTheme.appTypography.textSemiBold,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.onPrimary
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
            channelLogo = "https://iptvxpix.ml/vip-comedy.png",
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
            channelLogo = "https://iptvxpix.ml/vip-comedy.png",
            onClickAction = {}
        )
    }
}
