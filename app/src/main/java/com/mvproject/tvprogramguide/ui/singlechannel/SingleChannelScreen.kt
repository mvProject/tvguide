package com.mvproject.tvprogramguide.ui.singlechannel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SingleChannelScreen(
    channelId: String,
    channelName: String,
    onBackClick: () -> Unit
) {
    val singleChannelProgramsViewModel: SingleChannelProgramsViewModel = hiltViewModel()

    singleChannelProgramsViewModel.loadPrograms(channelId)

    val state = singleChannelProgramsViewModel.selectedPrograms.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithBack(
            title = channelName.parseChannelName(),
            onBackClick = onBackClick
        )

        SingleChannelData(singleChannelPrograms = state.value)
    }
}