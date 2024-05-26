package com.mvproject.tvprogramguide.ui.components.channels

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.ui.components.views.ChannelItem
import com.mvproject.tvprogramguide.ui.components.views.ProgramItem
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelList(
    listState: LazyListState,
    singleChannelPrograms: List<SelectedChannelWithPrograms>,
    onChannelClick: (SelectedChannel) -> Unit,
    onScheduleClick: (String, Program) -> Unit,
) {
    Crossfade(
        targetState = singleChannelPrograms,
        label = "ChannelList",
    ) { data ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.size4),
            state = listState,
        ) {
            data.forEach { item ->
                stickyHeader {
                    ChannelItem(
                        channelName = item.selectedChannel.channelName,
                        channelLogo = item.selectedChannel.channelIcon,
                    ) {
                        onChannelClick(item.selectedChannel)
                    }
                }
                items(
                    items = item.programs,
                    key = { program -> program.hashCode() },
                ) { program ->
                    ProgramItem(program = program) {
                        onScheduleClick(item.selectedChannel.channelName, program)
                    }
                }
            }
        }
    }
}
