package com.mvproject.tvprogramguide.ui.settings.channels.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.ui.settings.channels.actions.SelectedChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.components.SelectedChannelsList
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.SelectedChannelsViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectedChannelsScreen(
    selectedChannelsViewModel: SelectedChannelsViewModel = hiltViewModel()
) {
    val channels = selectedChannelsViewModel.selectedChannels.collectAsState().value

    SelectedChannelsContent(
        channels = channels,
        onAction = selectedChannelsViewModel::processAction
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun SelectedChannelsContent(
    channels: List<Channel>,
    onAction: (action: SelectedChannelsAction) -> Unit
) {
    Column {
        SelectedChannelsList(channels) { chn ->
            onAction(SelectedChannelsAction.ChannelDelete(chn))
        }
    }
}