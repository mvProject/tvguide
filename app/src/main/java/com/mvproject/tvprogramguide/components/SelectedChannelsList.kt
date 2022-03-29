package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectedChannelsList(channels: List<Channel>, onItemClick: (Channel) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.appColors.backgroundPrimary)
    ) {
        items(channels) { chn ->
            ChannelSelectableItem(
                channelLogo = chn.channelIcon,
                channelName = chn.channelName.parseChannelName()
            ) {
                onItemClick(chn)
            }
        }
    }
}

private val test = listOf(
    Channel(
        channelId = "channelId1",
        channelName = "channelName1",
        channelIcon = "channelIcon1",
        isSelected = false
    ),
    Channel(
        channelId = "channelId2",
        channelName = "channelName2",
        channelIcon = "channelIcon2",
        isSelected = false
    ),
    Channel(
        channelId = "channelId3",
        channelName = "channelName3",
        channelIcon = "channelIcon3",
        isSelected = false
    ),
    Channel(
        channelId = "channelId4",
        channelName = "channelName4",
        channelIcon = "channelIcon4",
        isSelected = false
    ),
    Channel(
        channelId = "channelId4",
        channelName = "channelName4",
        channelIcon = "channelIcon4",
        isSelected = false
    )
)


@Preview(showBackground = true)
@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun SelectedChannelsListItemViewDark() {
    TvGuideTheme(true) {
        SelectedChannelsList(
            channels = test
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun SelectedChannelsListItemView() {
    TvGuideTheme() {
        SelectedChannelsList(
            channels = test
        ) {

        }
    }
}