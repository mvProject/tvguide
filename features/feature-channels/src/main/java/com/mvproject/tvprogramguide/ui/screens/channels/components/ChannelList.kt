package com.mvproject.tvprogramguide.ui.screens.channels.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.core.ui.R
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelList(
    listState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    singleChannelPrograms: ImmutableList<SelectedChannelWithPrograms>,
    noEpgMessage: String,
    onChannelClick: (SelectionChannel) -> Unit,
    onScheduleClick: (String, Program) -> Unit,
) {
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
                    fallbackPainter = painterResource(R.drawable.no_channel_logo),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                ) {
                    if (item.programs.isNotEmpty()) {
                        onChannelClick(item.selectedChannel)
                    }
                }
            }
            if (item.programs.isEmpty()) {
                item(contentType = "empty") {
                    Text(
                        text = noEpgMessage,
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
                    key = { program -> program.programId + item.selectedChannel.channelId },
                    contentType = { "program" },
                ) { program ->
                    ProgramItem(program = program) {
                        onScheduleClick(item.selectedChannel.channelName, program)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@Composable
private fun ChannelListPreview() {
    TvGuideTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                ChannelList(
                    listState = rememberLazyListState(),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    noEpgMessage = "No EPG data found",
                    singleChannelPrograms = persistentListOf(
                        SelectedChannelWithPrograms(
                            selectedChannel = SelectionChannel(
                                channelId = "1",
                                channelName = "TV1000 Comedy",
                            ),
                            programs = listOf(
                                Program(
                                    programId = "p1",
                                    dateTimeStart = 0L,
                                    dateTimeEnd = 3600000L,
                                    title = "Morning Show",
                                ),
                                Program(
                                    programId = "p2",
                                    dateTimeStart = 3600000L,
                                    dateTimeEnd = 7200000L,
                                    title = "Evening News",
                                ),
                            ),
                        ),
                    ),
                    onChannelClick = {},
                    onScheduleClick = { _, _ -> },
                )
            }
        }
    }
}
