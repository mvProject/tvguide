package com.mvproject.tvprogramguide.ui.singlechannel.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.singlechannel.components.SingleChannelList
import com.mvproject.tvprogramguide.ui.singlechannel.viewmodel.SingleChannelViewModel
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import timber.log.Timber

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SingleChannelScreen(
    channelId: String,
    channelName: String,
    onBackClick: () -> Unit
) {
    val singleChannelProgramsViewModel: SingleChannelViewModel = hiltViewModel()

    SideEffect {
        Timber.i("testing SingleChannelScreen SideEffect")
    }

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

        SingleChannelList(singleChannelPrograms = state.value)
    }
}
