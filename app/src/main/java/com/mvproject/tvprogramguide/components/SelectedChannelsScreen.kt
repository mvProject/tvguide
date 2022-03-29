package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvproject.tvprogramguide.customlists.SelectedChannelsAction
import com.mvproject.tvprogramguide.customlists.SelectedChannelsViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectedChannelsScreen() {
    val selectedChannelsViewModel: SelectedChannelsViewModel = viewModel()
    Column {
        val state = selectedChannelsViewModel.selectedChannels.collectAsState()

        SelectedChannelsList(state.value) { chn ->
            selectedChannelsViewModel.applyAction(SelectedChannelsAction.ChannelDelete(chn))
        }
    }
}