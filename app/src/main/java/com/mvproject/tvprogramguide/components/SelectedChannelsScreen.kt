package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvproject.tvprogramguide.customlists.AllChannelViewModel
import com.mvproject.tvprogramguide.customlists.AvailableChannelsAction
import com.mvproject.tvprogramguide.customlists.SelectedChannelsAction
import com.mvproject.tvprogramguide.customlists.SelectedChannelsViewModel
import com.mvproject.tvprogramguide.theme.dimens

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SelectedChannelsScreen() {
    val selectedChannelsViewModel: SelectedChannelsViewModel = viewModel()
    Column {
        val state = selectedChannelsViewModel.selectedChannels.collectAsState()

        SelectedChannelsListItem(state.value) { chn ->
            selectedChannelsViewModel.applyAction(SelectedChannelsAction.ChannelDelete(chn))
        }
    }
}