package com.mvproject.tvprogramguide.ui.settings.channels.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.settings.channels.actions.SelectedChannelsAction
import com.mvproject.tvprogramguide.ui.settings.channels.components.ChannelSelectableItem
import com.mvproject.tvprogramguide.ui.settings.channels.components.DraggableItem
import com.mvproject.tvprogramguide.ui.settings.channels.components.dragContainer
import com.mvproject.tvprogramguide.ui.settings.channels.components.rememberDragDropState
import com.mvproject.tvprogramguide.ui.settings.channels.viewmodel.SelectedChannelsViewModel

@Composable
fun SelectedChannelsScreen(
    selectedChannelsViewModel: SelectedChannelsViewModel = hiltViewModel()
) {
    val channels by selectedChannelsViewModel.selectedChannels.collectAsStateWithLifecycle()
    SelectedChannelsContent(
        selectedChannels = channels,
        onAction = selectedChannelsViewModel::processAction
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectedChannelsContent(
    selectedChannels: List<SelectedChannel>,
    onAction: (action: SelectedChannelsAction) -> Unit
) {
    var channels by remember { mutableStateOf(selectedChannels) }

    LaunchedEffect(key1 = selectedChannels) {
        channels = selectedChannels
    }

    val listState = rememberLazyListState()

    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        onMoveEnd = {
            onAction(SelectedChannelsAction.ChannelsReorder(selectedChannels = channels))
        },
        onMove = { fromIndex, toIndex ->
            channels = channels.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .dragContainer(dragDropState)
            .fillMaxHeight(),
        state = listState,
        contentPadding = PaddingValues(MaterialTheme.dimens.size8),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)
    ) {
        itemsIndexed(channels, key = { _, chn -> chn }) { index, chn ->
            DraggableItem(dragDropState, index) { isDragging ->
                ChannelSelectableItem(
                    channelLogo = chn.channelIcon,
                    channelName = chn.channelName,
                    isDragged = isDragging
                ) {
                    onAction(SelectedChannelsAction.ChannelDelete(selectedChannel = chn))
                }
            }
        }
    }
}
