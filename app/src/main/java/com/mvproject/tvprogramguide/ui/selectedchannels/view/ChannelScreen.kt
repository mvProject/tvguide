package com.mvproject.tvprogramguide.ui.selectedchannels.view

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_COUNT
import com.mvproject.tvprogramguide.domain.utils.CHANNEL_INDEX
import com.mvproject.tvprogramguide.navigation.AppRoutes
import com.mvproject.tvprogramguide.ui.components.NoItemsScreen
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowSelectFromListDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithOptions
import com.mvproject.tvprogramguide.ui.selectedchannels.components.ChannelList
import com.mvproject.tvprogramguide.ui.selectedchannels.viewmodel.ChannelViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPermissionsApi::class)
@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onNavigate: (route: String) -> Unit
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val programState by viewModel.selectedPrograms.collectAsState()

    val alarmPermissionState = rememberPermissionState(
        Manifest.permission.SCHEDULE_EXACT_ALARM
    )

    val updateObserver = remember {
        Observer<List<WorkInfo>?> { listOfWorkInfo ->
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                Timber.e("worker updateWorkInfo null")
            } else {
                val workInfo = listOfWorkInfo.first()
                if (workInfo.state == WorkInfo.State.RUNNING) {
                    val progress = workInfo.progress
                    val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                    val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                    Timber.d("testing worker updateWorkInfo current $current, count $count")
                }
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    Timber.d("testing worker updateWorkInfo SUCCEEDED")
                    viewModel.reloadProgramsAfterUpdate()
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        with(viewModel) {
            checkForPartiallyUpdate()
            fullUpdateWorkInfo.observe(lifecycleOwner, updateObserver)

            onDispose {
                fullUpdateWorkInfo.removeObserver(updateObserver)
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithOptions(
                title = programState.listName,
                onSelectClick = {
                    isDialogOpen.value = true
                },
                onSettingsClick = { onNavigate(AppRoutes.OptionSettings.route) }
            )
        }
    ) { padding ->
        when {
            programState.listName.isEmpty() -> {
                NoItemsScreen(
                    title = stringResource(id = R.string.msg_user_filled_list_empty),
                    navigateTitle = stringResource(id = R.string.msg_tap_to_create_list),
                    onNavigateClick = { onNavigate(AppRoutes.UserCustomList.route) }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    ChannelList(
                        singleChannelPrograms = programState.programs,
                        listState = listState,
                        onChannelClick = { channel ->
                            onNavigate(
                                AppRoutes.SelectedChannel.applyArgs(
                                    channelId = channel.channelId,
                                    channelName = channel.channelName
                                )
                            )
                        },
                        onScheduleClick = { program ->
                            Timber.w("testing alarmPermissionState permission ${alarmPermissionState.status.isGranted}")
                            viewModel.toggleProgramSchedule(programForSchedule = program)
                        }
                    )
                }
            }
        }

        ShowSelectFromListDialog(
            isDialogOpen = isDialogOpen,
            radioOptions = viewModel.availableLists,
            defaultSelection = viewModel.obtainSelectedListIndex
        ) { selected ->
            viewModel.applyList(listName = selected)
        }
    }
}
