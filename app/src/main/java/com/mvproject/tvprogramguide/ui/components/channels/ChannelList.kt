package com.mvproject.tvprogramguide.ui.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.ui.components.views.ChannelItem
import com.mvproject.tvprogramguide.ui.components.views.ProgramItem
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelList(
    listState: LazyListState,
    singleChannelPrograms: List<SelectedChannelWithPrograms>,
    onChannelClick: (SelectionChannel) -> Unit,
    onScheduleClick: (String, Program) -> Unit,
) {
/*    Crossfade(
        targetState = singleChannelPrograms,
        label = "ChannelList",
    ) { data ->*/
        LazyColumn(
            modifier =
            Modifier
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.size4),
            state = listState,
        ) {
            singleChannelPrograms.forEach { item ->
                stickyHeader(
                    key = item.selectedChannel.channelId,
                ) {
                    ChannelItem(
                        channelName = item.selectedChannel.channelName,
                        channelLogo = item.selectedChannel.channelIcon,
                    ) {
                        onChannelClick(item.selectedChannel)
                    }
                }
                if (item.programs.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.msg_no_epg_found),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.size10)
                                .padding(start = MaterialTheme.dimens.size16),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    items(
                        items = item.programs,
                        key = { program -> program.programId },
                    ) { program ->
                        ProgramItem(program = program) {
                            onScheduleClick(item.selectedChannel.channelName, program)
                        }
                    }
                }
            }
        }
    //}
}
