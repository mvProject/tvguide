package com.mvproject.tvprogramguide.ui.settings.channels.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
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

@ExperimentalMaterialApi
@Composable
fun ChannelSelectableItem(
    channelName: String,
    channelLogo: String,
    isSelected: Boolean = true,
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
            fontSize = MaterialTheme.dimens.font14,
            style = MaterialTheme.appTypography.textMedium,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.appColors.textPrimary
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Icon(
            imageVector = if (isSelected) Icons.Outlined.Delete else Icons.Outlined.Add,
            tint = if (isSelected) MaterialTheme.appColors.tintSecondary else MaterialTheme.appColors.tintPrimary,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@ExperimentalMaterialApi
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

@ExperimentalMaterialApi
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