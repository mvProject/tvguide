package com.mvproject.tvprogramguide.ui.singlechannel.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.singlechannel.components.SingleChannelList
import com.mvproject.tvprogramguide.ui.singlechannel.viewmodel.SingleChannelViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SingleChannelScreen(
    viewModel: SingleChannelViewModel,
    channelId: String,
    channelName: String,
    onBackClick: () -> Unit
) {
    viewModel.loadPrograms(channelId)

    val state = viewModel.selectedPrograms.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithBack(
            title = channelName,
            onBackClick = onBackClick
        )

        SingleChannelList(singleChannelPrograms = state.value)
    }
}
