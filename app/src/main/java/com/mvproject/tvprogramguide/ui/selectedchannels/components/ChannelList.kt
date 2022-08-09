package com.mvproject.tvprogramguide.ui.selectedchannels.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannel
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.ui.components.ProgramItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelList(
    singleChannelPrograms: List<SelectedChannelWithPrograms>,
    onChannelClick: (SelectedChannel) -> Unit,
    onScheduleClick: (ProgramSchedule) -> Unit
) {
    Crossfade(targetState = singleChannelPrograms) { data ->
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colors.primary)
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
                items(item.programs) { program ->
                    ProgramItem(
                        program = program
                    ) {
                        val programSchedule = ProgramSchedule(
                            channelId = program.channel,
                            programTitle = program.title,
                        )
                        onScheduleClick(programSchedule)
                    }
                }
            }
        }
    }
}
