package com.mvproject.tvprogramguide.ui.settings.channels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.settings.channels.components.ChannelSelectableItem
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun AllChannelsList(
    channels: List<Channel>,
    onItemClick: (Channel) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary),
    ) {
        items(channels) { chn ->
            ChannelSelectableItem(
                channelLogo = chn.channelIcon,
                channelName = chn.channelName.parseChannelName(),
                isSelected = chn.isSelected
            ) {
                onItemClick(chn)
            }
        }
    }
}