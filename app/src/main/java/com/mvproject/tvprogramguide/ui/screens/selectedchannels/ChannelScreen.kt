package com.mvproject.tvprogramguide.ui.screens.selectedchannels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.channels.ChannelList
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowSelectFromListDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithOptions
import com.mvproject.tvprogramguide.ui.components.views.NoItemsScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    onNavigateSingleChannel: (String, String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateChannelsList: () -> Unit
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.reloadData()

        onPauseOrDispose {

        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithOptions(
                title = viewState.listName,
                isSelectEnabled = viewState.isNotSinglePlaylist,
                onSelectClick = {
                    isDialogOpen.value = true
                },
                onSettingsClick = onNavigateSettings
            )
        }
    ) { padding ->
        when {
            viewState.listName.isEmpty() -> {
                NoItemsScreen(
                    title = stringResource(id = R.string.msg_user_filled_list_empty),
                    navigateTitle = stringResource(id = R.string.msg_tap_to_create_list),
                    onNavigateClick = onNavigateChannelsList
                )
            }

            else -> {

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    ChannelList(
                        singleChannelPrograms = viewState.playlistContent.channels,
                        listState = listState,
                        onChannelClick = { channel ->
                            onNavigateSingleChannel(
                                channel.channelId,
                                channel.channelName
                            )
                        },
                        onScheduleClick = viewModel::toggleProgramSchedule
                    )
                }
            }
        }

        ShowSelectFromListDialog(
            isDialogOpen = isDialogOpen,
            radioOptions = viewState.listNames,
            defaultSelection = viewState.selectedListIndex,
            onSelected = viewModel::applyList
        )
    }
}
