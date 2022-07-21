package com.mvproject.tvprogramguide.ui.settings.channels.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.ui.settings.channels.actions.SelectedChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.components.SelectedChannelsList
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.SelectedChannelsViewModel

@Composable
fun SelectedChannelsScreen(
    selectedChannelsViewModel: SelectedChannelsViewModel = hiltViewModel()
) {
    val channels = selectedChannelsViewModel.selectedChannels.collectAsState().value
    SelectedChannelsContent(
        selectedChannels = channels,
        onAction = selectedChannelsViewModel::processAction
    )
}

@Composable
private fun SelectedChannelsContent(
    selectedChannels: List<SelectedChannel>,
    onAction: (action: SelectedChannelsAction) -> Unit
) {
    Column {
        SelectedChannelsList(selectedChannels) { chn ->
            onAction(SelectedChannelsAction.ChannelDelete(selectedChannel = chn))
        }
    }
}
