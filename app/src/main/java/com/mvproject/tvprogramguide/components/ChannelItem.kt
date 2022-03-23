package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mvproject.tvprogramguide.R

@Composable
fun ChannelItem(channelName:String,channelLogo:String, onClickAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = colorResource(id = R.color.midnightblue))
            .padding(8.dp)
            .clickable {
                onClickAction()
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = channelLogo),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(4.dp))

        )

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Text(
            text = channelName,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = colorResource(id = R.color.whitesmoke)
        )
    }
}

@Preview
@Composable
fun ChannelItemView() {
    ChannelItem(
        channelName = "TV1000 Comedy",
        channelLogo = "https://iptvxpix.ml/vip-comedy.png",
        onClickAction = {}
    )
}