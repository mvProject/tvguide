package com.mvproject.tvprogramguide.ui.singlechannel.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.schedule.ProgramSchedule
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.ProgramItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleChannelList(
    listState: LazyListState,
    singleChannelPrograms: List<SingleChannelWithPrograms>,
    onScheduleClick: (ProgramSchedule) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.size4),
        state = listState
    ) {
        singleChannelPrograms.forEach { item ->
            stickyHeader {
                DateItem(date = item.date)
            }
            items(item.programs) { program ->
                ProgramItem(
                    program = program
                ) {
                    val programSchedule = ProgramSchedule(
                        programTitle = program.title,
                        channelId = program.channel
                    )
                    onScheduleClick(programSchedule)
                }
            }
        }
    }
}
