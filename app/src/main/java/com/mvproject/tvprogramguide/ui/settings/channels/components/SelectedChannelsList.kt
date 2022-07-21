package com.mvproject.tvprogramguide.ui.settings.channels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel

@Composable
fun SelectedChannelsList(
    selectedChannels: List<SelectedChannel>,
    onItemClick: (SelectedChannel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
    ) {
        items(selectedChannels) { chn ->
            ChannelSelectableItem(
                channelLogo = chn.channelIcon,
                channelName = chn.channelName
            ) {
                onItemClick(chn)
            }
        }
    }
}
