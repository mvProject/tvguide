package com.mvproject.tvprogramguide.ui.screens.channels.single

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
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.DateItem
import com.mvproject.tvprogramguide.ui.components.views.ProgramItem
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleChannelScreen(
    viewModel: SingleChannelViewModel,
    onNavigateBack: () -> Unit,
) {
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
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding =
                    PaddingValues(
                        horizontal = MaterialTheme.dimens.size4,
                    ),
                state = listState,
            ) {
                viewModel.selectedPrograms.forEach { item ->
                    stickyHeader {
                        DateItem(date = item.date)
                    }
                    items(
                        items = item.programs,
                        key = { program -> program.hashCode() },
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
