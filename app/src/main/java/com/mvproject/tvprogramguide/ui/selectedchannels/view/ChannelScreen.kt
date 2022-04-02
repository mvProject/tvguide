package com.mvproject.tvprogramguide.ui.selectedchannels.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.components.dialogs.ShowSelectDialog
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithOptions
import com.mvproject.tvprogramguide.data.model.Channel
import com.mvproject.tvprogramguide.ui.selectedchannels.components.ChannelList
import com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel.ChannelViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ChannelScreen(
    onSettingsClick: () -> Unit,
    onChannelClick: (Channel) -> Unit
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    val channelViewModel: ChannelViewModel = hiltViewModel()

    val selectedListState = channelViewModel.selectedList.collectAsState()
    val selectedPrograms = channelViewModel.selectedPrograms.collectAsState()

    channelViewModel.checkAvailableChannelsUpdate()
    channelViewModel.checkFullProgramsUpdate()
    channelViewModel.checkForUpdates()
    channelViewModel.checkSavedList()
    channelViewModel.reloadChannels()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithOptions(
            title = selectedListState.value,
            onSelectClick = {
                isDialogOpen.value = true
            },
            onSettingsClick = onSettingsClick
        )

        ChannelList(selectedPrograms.value) {
            onChannelClick(it)
        }

        ShowSelectDialog(
            isDialogOpen = isDialogOpen,
            radioOptions = channelViewModel.availableLists,
            defaultSelection = channelViewModel.obtainListIndex
        ) { selected ->
            channelViewModel.saveSelectedList(selected)
        }
    }

    /*
    if (name.isNotEmpty()) {
                    programsViewModel.partiallyUpdateWorkInfo.observe(
                        viewLifecycleOwner
                    ) { listOfWorkInfo: List<WorkInfo>? ->
                        if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                            Timber.d("testing worker partiallyUpdateWorkInfo null")
                        } else {
                            val workInfo = listOfWorkInfo[0]
                            if (workInfo.state == WorkInfo.State.RUNNING) {
                                val progress = workInfo.progress
                                val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                                val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                                showUpdateProgress(current, count)
                            }
                            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                showUpdateComplete()
                            }
                        }
                    }
                    programsViewModel.fullUpdateWorkInfo.observe(
                        viewLifecycleOwner
                    ) { listOfWorkInfo: List<WorkInfo>? ->
                        if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                            Timber.d("testing worker fullUpdateWorkInfo null")
                        } else {
                            val workInfo = listOfWorkInfo[0]
                            if (workInfo.state == WorkInfo.State.RUNNING) {
                                val progress = workInfo.progress
                                val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                                val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                                showUpdateProgress(current, count)
                            }
                            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                showUpdateComplete()
                            }
                        }
                    }
     */
}