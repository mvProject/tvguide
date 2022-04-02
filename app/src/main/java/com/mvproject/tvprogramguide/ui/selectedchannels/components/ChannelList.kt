package com.mvproject.tvprogramguide.ui.selectedchannels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.components.ProgramItem
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.data.model.SelectedChannelModel
import com.mvproject.tvprogramguide.utils.Utils.calculateProgramProgress
import com.mvproject.tvprogramguide.utils.Utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ChannelList(
    singleChannelPrograms: List<SelectedChannelModel>,
    onChannelClick: (Channel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
    ) {
        singleChannelPrograms.forEach {
            stickyHeader {
                ChannelItem(
                    channelName = it.channel.channelName.parseChannelName(),
                    channelLogo = it.channel.channelIcon
                ) {
                    onChannelClick(it.channel)
                }
            }
            items(it.programs) { program ->
                ProgramItem(
                    prgTime = program.dateTimeStart.convertTimeToReadableFormat(),
                    prgTitle = program.title,
                    prgDescription = program.description,
                    progressValue = calculateProgramProgress(
                        program.dateTimeStart,
                        program.dateTimeEnd
                    )
                )
            }
        }
    }
}