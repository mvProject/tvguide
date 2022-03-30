package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ChannelItem(
    channelName: String,
    channelLogo: String,
    onClickAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = MaterialTheme.appColors.backgroundPrimary)
            .padding(MaterialTheme.dimens.size8)
            .clickable {
                onClickAction()
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = channelLogo),
            contentDescription = null,
            modifier = Modifier
                .size(MaterialTheme.dimens.size38)
                .clip(RoundedCornerShape(MaterialTheme.dimens.size4))

        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Text(
            text = channelName,
            fontSize = MaterialTheme.dimens.font16,
            style = MaterialTheme.appTypography.textSemiBold,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.appColors.textPrimary
        )
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