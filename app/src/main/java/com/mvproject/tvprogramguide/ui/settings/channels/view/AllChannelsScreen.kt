package com.mvproject.tvprogramguide.ui.settings.channels.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.components.search.SearchView
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.settings.channels.actions.AvailableChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.components.AllChannelsList
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.AllChannelViewModel
import timber.log.Timber

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun AllChannelsScreen(
    allChannelViewModel: AllChannelViewModel = hiltViewModel()
) {
    SideEffect {
        Timber.i("testing AllChannelsScreen SideEffect")
    }
    val channels = allChannelViewModel.allChannels.collectAsState().value
    AllChannelsContent(
        channels = channels,
        onAction = allChannelViewModel::processAction
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun AllChannelsContent(
    channels: List<Channel>,
    onAction: (action: AvailableChannelsAction) -> Unit
) {
    SideEffect {
        Timber.i("testing AllChannelsContent SideEffect")
    }
    Column {
        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8)
        ) { query ->
            onAction(AvailableChannelsAction.ChannelFilter(query))
        }

        AllChannelsList(channels) { channel ->

            if (channel.isSelected) {
                onAction(
                    AvailableChannelsAction.ChannelDelete(channel)
                )
            } else {
                onAction(
                    AvailableChannelsAction.ChannelAdd(channel)
                )
            }
        }
    }
}