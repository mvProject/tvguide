package com.mvproject.tvprogramguide.ui.screens.settings.channels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.ui.components.composables.DraggableItem
import com.mvproject.tvprogramguide.ui.components.composables.dragContainer
import com.mvproject.tvprogramguide.ui.components.composables.rememberDragDropState
import com.mvproject.tvprogramguide.ui.components.views.ChannelSelectableItem
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectedChannelsPage(
    selectedChannels: List<SelectionChannel>,
    onAction: (action: ChannelsAction) -> Unit,
) {
    var channels by remember { mutableStateOf(selectedChannels) }

    LaunchedEffect(key1 = selectedChannels) {
        channels = selectedChannels
    }

    val listState = rememberLazyListState()

    val dragDropState =
        rememberDragDropState(
            lazyListState = listState,
            onMoveEnd = {
                onAction(ChannelsAction.ChannelsReorder(selectedChannels = channels))
            },
            onMove = { fromIndex, toIndex ->
                channels =
                    channels.toMutableList().apply {
                        add(toIndex, removeAt(fromIndex))
                    }
            },
        )

    LazyColumn(
        modifier =
            Modifier
                .dragContainer(dragDropState)
                .fillMaxHeight(),
        state = listState,
        contentPadding =
            PaddingValues(
                vertical = MaterialTheme.dimens.size8,
                horizontal = MaterialTheme.dimens.size4,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
    ) {
        itemsIndexed(channels, key = { _, chn -> chn.channelId }) { index, chn ->
            DraggableItem(dragDropState, index) { isDragging ->
                ChannelSelectableItem(
                    channelLogo = chn.channelIcon,
                    channelName = chn.channelName,
                    isDragged = isDragging,
                ) {
                    onAction(ChannelsAction.DeleteSelection(selectedChannel = chn))
                }
            }
        }
    }
}
