package com.mvproject.tvprogramguide.ui.screens.singlechannel.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.ui.components.channels.SingleChannelList
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.screens.singlechannel.viewmodel.SingleChannelViewModel

@Composable
fun SingleChannelScreen(
    viewModel: SingleChannelViewModel,
    onNavigateBack: () -> Unit
) {
    val programs by viewModel.selectedPrograms.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = viewModel.name,
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            SingleChannelList(
                singleChannelPrograms = programs,
                listState = listState
            ) { program ->
                viewModel.toggleProgramSchedule(programForSchedule = program)
            }
        }
    }
}
