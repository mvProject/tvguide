package com.mvproject.tvprogramguide.ui.screens.settings.channels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.ui.components.search.SearchView
import com.mvproject.tvprogramguide.ui.components.views.ChannelSelectableItem
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailableChannelsPage(
    selectedChannels: List<SelectionChannel>,
    onAction: (action: ChannelsAction) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size10),
    ) {
        SearchView(
            modifier = Modifier.fillMaxWidth(),
        ) { selectedQuery ->
            onAction(ChannelsAction.ChannelFilter(query = selectedQuery))
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .imeNestedScroll(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size6),
        ) {
            items(
                items = selectedChannels,
                key = { item -> item.channelId },
            ) { chn ->
                ChannelSelectableItem(
                    channelLogo = chn.channelIcon,
                    channelName = chn.channelName,
                    isSelected = chn.isSelected,
                ) {
                    onAction(ChannelsAction.ToggleSelection(channel = chn))
                }
            }
        }
    }
}
