package com.mvproject.tvprogramguide.ui.screens.settings.channels.available

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.ui.components.channels.AllChannelsList
import com.mvproject.tvprogramguide.ui.components.search.SearchView
import com.mvproject.tvprogramguide.ui.screens.settings.channels.available.action.AvailableChannelsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun AllChannelsScreen(
    allChannelViewModel: AllChannelViewModel = hiltViewModel()
) {
    val channels by allChannelViewModel.availableChannels.collectAsStateWithLifecycle()
    AllChannelsContent(
        selectedChannels = channels,
        onAction = allChannelViewModel::processAction
    )
}

@Composable
private fun AllChannelsContent(
    selectedChannels: List<AvailableChannel>,
    onAction: (action: AvailableChannelsAction) -> Unit
) {
    val listState = rememberLazyListState()

    Column {
        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8)
        ) { selectedQuery ->
            onAction(AvailableChannelsAction.ChannelFilter(query = selectedQuery))
        }
        AllChannelsList(
            state = listState,
            selectedChannels = selectedChannels
        ) { channel ->
            if (channel.isSelected) {
                onAction(
                    AvailableChannelsAction.ChannelDelete(selectedChannel = channel)
                )
            } else {
                onAction(
                    AvailableChannelsAction.ChannelAdd(selectedChannel = channel)
                )
            }
        }
    }
}
