package com.mvproject.tvprogramguide.ui.settings.channels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllChannelsList(
    selectedChannels: List<AvailableChannel>,
    onItemClick: (AvailableChannel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .imePadding()
            .imeNestedScroll(),
    ) {
        items(selectedChannels) { chn ->
            ChannelSelectableItem(
                channelLogo = chn.channelIcon,
                channelName = chn.channelName,
                isSelected = chn.isSelected
            ) {
                onItemClick(chn)
            }
        }
    }
}
