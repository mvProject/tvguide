package com.mvproject.tvprogramguide.ui.screens.channels.single

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.DateItem
import com.mvproject.tvprogramguide.ui.components.views.ProgramItem
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.closeScreenAnimation
import com.mvproject.tvprogramguide.utils.openScreenAnimation
import com.mvproject.tvprogramguide.utils.slideOutDetailsBoundsTransform

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SingleChannelScreen(
    viewModel: SingleChannelViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit,
) {
    val selectedPrograms by viewModel.selectedPrograms.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = viewModel.name,
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
                        sharedContentState = rememberSharedContentState(key = viewModel.name),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = openScreenAnimation,
                        exit = closeScreenAnimation,
                        boundsTransform = slideOutDetailsBoundsTransform,
                    ),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.dimens.size4,
                    ),
                    state = listState,
                ) {
                    selectedPrograms.forEach { item ->
                        stickyHeader {
                            DateItem(date = item.date)
                        }
                        items(
                            items = item.programs,
                            key = { program -> program.programId },
                        ) { program ->
                            ProgramItem(program = program) {
                                viewModel.toggleSchedule(
                                    channelName = viewModel.name,
                                    program = program,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
