package com.mvproject.tvprogramguide.ui.settings.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.ui.settings.channels.SelectedChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.SelectedChannelsList
import com.mvproject.tvprogramguide.ui.settings.channels.SelectedChannelsViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectedChannelsScreen() {
    val selectedChannelsViewModel: SelectedChannelsViewModel = hiltViewModel()
    Column {
        val state = selectedChannelsViewModel.selectedChannels.collectAsState()

        SelectedChannelsList(state.value) { chn ->
            selectedChannelsViewModel.applyAction(SelectedChannelsAction.ChannelDelete(chn))
        }
    }
}