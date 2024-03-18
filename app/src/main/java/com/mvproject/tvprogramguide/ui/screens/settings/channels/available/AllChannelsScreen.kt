package com.mvproject.tvprogramguide.ui.screens.settings.channels.available

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.ui.components.search.SearchView
import com.mvproject.tvprogramguide.ui.components.views.ChannelSelectableItem
import com.mvproject.tvprogramguide.ui.screens.settings.channels.available.action.AvailableChannelsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun AllChannelsScreen(allChannelViewModel: AllChannelViewModel = hiltViewModel()) {
    val channels by allChannelViewModel.availableChannels.collectAsStateWithLifecycle()

    AllChannelsContent(
        selectedChannels = channels,
        onAction = allChannelViewModel::processAction,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AllChannelsContent(
    selectedChannels: List<AvailableChannel>,
    onAction: (action: AvailableChannelsAction) -> Unit,
) {
    val listState = rememberLazyListState()

    Column {
        SearchView(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size8),
        ) { selectedQuery ->
            onAction(AvailableChannelsAction.ChannelFilter(query = selectedQuery))
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .imePadding()
                    .imeNestedScroll(),
            state = listState,
            contentPadding =
                PaddingValues(
                    vertical = MaterialTheme.dimens.size8,
                    horizontal = MaterialTheme.dimens.size4,
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
        ) {
            items(selectedChannels) { chn ->
                ChannelSelectableItem(
                    channelLogo = chn.channelIcon,
                    channelName = chn.channelName,
                    isSelected = chn.isSelected,
                ) {
                    onAction(
                        if (chn.isSelected) {
                            AvailableChannelsAction.ChannelDelete(selectedChannel = chn)
                        } else {
                            AvailableChannelsAction.ChannelAdd(selectedChannel = chn)
                        },
                    )
                }
            }
        }
    }
}
