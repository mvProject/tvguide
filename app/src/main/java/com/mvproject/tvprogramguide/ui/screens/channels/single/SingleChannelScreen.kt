package com.mvproject.tvprogramguide.ui.screens.channels.single

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.domain.SingleChannelWithPrograms
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.DateItem
import com.mvproject.tvprogramguide.ui.components.views.ProgramItem
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.containerTransformBoundsTransform
import com.mvproject.tvprogramguide.utils.sharedBoundsEnter
import com.mvproject.tvprogramguide.utils.sharedBoundsExit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SingleChannelScreen(
    viewModel: SingleChannelViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit,
) {
    val selectedPrograms by viewModel.selectedPrograms.collectAsStateWithLifecycle()
    SingleChannelContent(
        name = viewModel.name,
        programs = selectedPrograms,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onNavigateBack = onNavigateBack,
        onScheduleClick = { viewModel.toggleSchedule(viewModel.name, it) },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SingleChannelContent(
    name: String,
    programs: ImmutableList<SingleChannelWithPrograms>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit,
    onScheduleClick: (Program) -> Unit = {},
) {
    val listState = rememberLazyListState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = name,
                onBackClick = onNavigateBack,
            )
        },
    ) { padding ->
        with(sharedTransitionScope) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = name),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = sharedBoundsEnter,
                        exit = sharedBoundsExit,
                        boundsTransform = containerTransformBoundsTransform,
                    ),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.dimens.size4,
                    ),
                    state = listState,
                ) {
                    programs.forEach { item ->
                        stickyHeader {
                            DateItem(date = item.date)
                        }
                        items(
                            items = item.programs,
                            key = { program -> program.programId },
                        ) { program ->
                            ProgramItem(program = program) {
                                onScheduleClick(program)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@Composable
private fun SingleChannelContentPreview() {
    TvGuideTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                SingleChannelContent(
                    name = "TV1000 Comedy",
                    programs = persistentListOf(
                        SingleChannelWithPrograms(
                            date = "Monday, 21 March",
                            programs = listOf(
                                Program(
                                    programId = "p1",
                                    dateTimeStart = 0L,
                                    dateTimeEnd = 3600000L,
                                    title = "Morning Show",
                                    description = "Start the day with great content",
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
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    onNavigateBack = {},
                )
            }
        }
    }
}
