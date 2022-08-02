package com.mvproject.tvprogramguide.ui.settings.channels.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.search.SearchView
import com.mvproject.tvprogramguide.ui.settings.channels.actions.AvailableChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.components.AllChannelsList
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.AllChannelViewModel

@Composable
fun AllChannelsScreen(
    allChannelViewModel: AllChannelViewModel = hiltViewModel()
) {
    val channels by allChannelViewModel.availableChannels.collectAsState()
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
    Column {
        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8)
        ) { selectedQuery ->
            onAction(AvailableChannelsAction.ChannelFilter(query = selectedQuery))
        }
        AllChannelsList(selectedChannels) { channel ->
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
