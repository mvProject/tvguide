package com.mvproject.tvprogramguide.ui.selectedchannels.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.work.WorkInfo
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.NoItemsScreen
import com.mvproject.tvprogramguide.components.dialogs.ShowSelectDialog
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithOptions
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.ui.selectedchannels.components.ChannelList
import com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel.ChannelViewModel
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    onNavigate: (route: String) -> Unit
) {

    val owner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = true) {
        Timber.i("testing ChannelScreen LaunchedEffect")
        // channelViewModel.partiallyUpdateWorkInfo.observe(
        //     owner
        // ) { listOfWorkInfo: List<WorkInfo>? ->
        //     if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
        //         Timber.d("testing worker partiallyUpdateWorkInfo null")
        //     } else {
        //         val workInfo = listOfWorkInfo[0]
        //         if (workInfo.state == WorkInfo.State.RUNNING) {
        //             val progress = workInfo.progress
        //             val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
        //             val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
        //             Timber.d(
        //                 "testing worker partiallyUpdateWorkInfo " +
        //                         "current $current, count $count"
        //             )
        //         }
        //         if (workInfo.state == WorkInfo.State.SUCCEEDED) {
        //             channelViewModel.reloadChannels()
        //             Timber.d("testing worker partiallyUpdateWorkInfo SUCCEEDED")
        //         }
        //     }
        // }
        with(viewModel) {
            reloadChannels()

            checkFullProgramsUpdate()

            fullUpdateWorkInfo.observe(
                owner
            ) { listOfWorkInfo: List<WorkInfo>? ->
                if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                    Timber.e("testing worker fullUpdateWorkInfo null")
                } else {
                    val workInfo = listOfWorkInfo[0]
                    if (workInfo.state == WorkInfo.State.RUNNING) {
                        val progress = workInfo.progress
                        val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                        val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                        Timber.d(
                            "testing worker fullUpdateWorkInfo " +
                                    "current $current, count $count"
                        )
                    }
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        viewModel.reloadChannels()
                        Timber.d("testing worker fullUpdateWorkInfo SUCCEEDED")
                    }
                }
            }
        }
    }

    val isDialogOpen = remember { mutableStateOf(false) }

    val programState = viewModel.selectedPrograms.collectAsState().value

    when {
        programState.listName.isEmpty() -> {
            NoItemsScreen(
                title = stringResource(id = R.string.user_filled_list_empty),
                navigateTitle = stringResource(id = R.string.tap_to_create_list),
                onNavigateClick = { onNavigate(AppRoutes.UserCustomList.route) }
            )
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ToolbarWithOptions(
                    title = programState.listName,
                    onSelectClick = {
                        isDialogOpen.value = true
                    },
                    onSettingsClick = { onNavigate(AppRoutes.OptionSettings.route) }
                )

                ChannelList(programState.programs) { channel ->
                    onNavigate(
                        AppRoutes.SelectedChannel.applyArgs(
                            channelId = channel.channelId,
                            channelName = channel.channelName
                        )
                    )
                }
            }
        }
    }

    ShowSelectDialog(
        isDialogOpen = isDialogOpen,
        radioOptions = viewModel.availableLists,
        defaultSelection = viewModel.obtainListIndex
    ) { selected ->
        viewModel.applyList(listName = selected)
    }
}
