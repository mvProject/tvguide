package com.mvproject.tvprogramguide.ui.screens.selectedchannels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.channels.ChannelList
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowSelectFromListDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithOptions
import com.mvproject.tvprogramguide.ui.components.views.NoItemsScreen
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.REFRESH_DELAY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
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

    val state = rememberPullToRefreshState(
        positionalThreshold = MaterialTheme.dimens.size110
    )
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.forceReloadData()
            delay(REFRESH_DELAY)
            state.endRefresh()
        }
    }

    LifecycleResumeEffect(Unit) {
        viewModel.reloadData()

        onPauseOrDispose {

        }
    }

    val scope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex >= 5
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithOptions(
                title = viewState.listName,
                isSelectEnabled = viewState.isNotSinglePlaylist,
                scrollBehavior = scrollBehavior,
                onSelectClick = {
                    isDialogOpen.value = true
                },
                onSettingsClick = onNavigateSettings
            )
        },
        floatingActionButton = {
            if (showScrollToTopButton) {
                FloatingActionButton(onClick = {
                    scope.launch {
                        listState.animateScrollToItem(COUNT_ZERO)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }
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

                Box(Modifier.nestedScroll(state.nestedScrollConnection)) {
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
                    PullToRefreshContainer(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = state,
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
