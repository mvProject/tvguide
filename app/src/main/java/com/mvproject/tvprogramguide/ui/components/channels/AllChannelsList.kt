package com.mvproject.tvprogramguide.ui.components.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllChannelsList(
    state: LazyListState,
    selectedChannels: List<AvailableChannel>,
    onItemClick: (AvailableChannel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .imeNestedScroll(),
        state = state,
        contentPadding = PaddingValues(MaterialTheme.dimens.size8),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)
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
