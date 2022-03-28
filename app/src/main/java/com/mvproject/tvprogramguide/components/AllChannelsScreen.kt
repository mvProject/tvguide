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
import com.mvproject.tvprogramguide.theme.dimens

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun AllChannelsScreen() {
    val allChannelViewModel: AllChannelViewModel = viewModel()
    Column {
        val state = allChannelViewModel.allChannels.collectAsState()

        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8)
        ) {
            allChannelViewModel.processAction(AvailableChannelsAction.ChannelFilter(it))
        }

        AllChannelsListItem(state.value) { chn ->

            if (chn.isSelected) {
                allChannelViewModel.processAction(
                    AvailableChannelsAction.ChannelDelete(
                        chn
                    )
                )
            } else {
                allChannelViewModel.processAction(
                    AvailableChannelsAction.ChannelAdd(
                        chn
                    )
                )
            }
        }
    }
}