package com.mvproject.tvprogramguide.ui.screens.settings.channels

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.feature.settings.R
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.screens.settings.channels.components.AvailableChannelsPage
import com.mvproject.tvprogramguide.ui.screens.settings.channels.components.SelectedChannelsPage
import com.mvproject.tvprogramguide.ui.screens.settings.channels.state.ChannelSettingsState
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.utils.AppConstants.SELECTED_CHANNELS_PAGE
import com.mvproject.tvprogramguide.utils.containerTransformBoundsTransform
import com.mvproject.tvprogramguide.utils.sharedBoundsEnter
import com.mvproject.tvprogramguide.utils.sharedBoundsExit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelSettingsScreen(
    viewModel: ChannelSettingsViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val selected by viewModel.selected.collectAsStateWithLifecycle()
    val allChannels by viewModel.allChannels.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.applyChanges()
    }

    ChannelSettingsContent(
        name = viewModel.name,
        viewState = viewState,
        selected = selected,
        allChannels = allChannels,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onNavigateBack = onNavigateBack,
        onApplyChanges = viewModel::applyChanges,
        onAction = viewModel::processAction,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelSettingsContent(
    name: String,
    viewState: ChannelSettingsState,
    selected: ImmutableList<SelectionChannel>,
    allChannels: ImmutableList<SelectionChannel>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit = {},
    onApplyChanges: () -> Unit = {},
    onAction: (ChannelsAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val currentOnNavigateBack by rememberUpdatedState(onNavigateBack)
    LaunchedEffect(viewState.isComplete) {
        if (viewState.isComplete) {
            currentOnNavigateBack()
        }
    }

    val tabItems =
        listOf(
            stringResource(id = R.string.title_all_channels),
            stringResource(id = R.string.title_selected_channels),
        )
    val pagerState =
        rememberPagerState(
            initialPage = COUNT_ZERO,
            initialPageOffsetFraction = COUNT_ZERO_FLOAT,
        ) {
            tabItems.size
        }

    val inActiveTabColor = MaterialTheme.colorScheme.inverseOnSurface
    val activeTabColor = MaterialTheme.colorScheme.onSurfaceVariant
    val inActiveTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeTextColor = MaterialTheme.colorScheme.inverseOnSurface

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.settings_channels_settings_title),
                onBackClick = onApplyChanges,
            )
        },
    ) { padding ->
        with(sharedTransitionScope) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = name),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = sharedBoundsEnter,
                        exit = sharedBoundsExit,
                        boundsTransform = containerTransformBoundsTransform,
                    ),
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier =
                        Modifier
                            .padding(MaterialTheme.dimens.size4)
                            .clip(MaterialTheme.shapes.medium),
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    indicator = {},
                    divider = {},
                ) {
                    tabItems.forEachIndexed { index, title ->
                        val tabColor = remember { Animatable(activeTabColor) }
                        val textColor = remember { Animatable(activeTextColor) }

                        LaunchedEffect(pagerState.currentPage == index) {
                            val selectedTabColor =
                                if (pagerState.currentPage == index) {
                                    activeTabColor
                                } else {
                                    inActiveTabColor
                                }

                            tabColor.animateTo(selectedTabColor)

                            val selectedTextColor =
                                if (pagerState.currentPage == index) {
                                    activeTextColor
                                } else {
                                    inActiveTextColor
                                }
                            textColor.animateTo(selectedTextColor)
                        }

                        Tab(
                            modifier =
                                Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .drawBehind {
                                        drawRect(color = tabColor.value)
                                    },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                val textStyle =
                                    if (pagerState.currentPage == index) {
                                        MaterialTheme.typography.titleMedium
                                    } else {
                                        MaterialTheme.typography.bodyMedium
                                    }
                                Text(
                                    text = title,
                                    style = textStyle,
                                    color = textColor.value,
                                )
                            },
                        )
                    }
                }

                val channelsList = remember(allChannels, viewState.searchString) {
                    if (viewState.searchString.length > COUNT_ONE) {
                        allChannels.filter {
                            it.channelName.contains(viewState.searchString, true)
                        }.toImmutableList()
                    } else {
                        allChannels
                    }
                }

                HorizontalPager(
                    modifier = Modifier,
                    state = pagerState,
                    pageNestedScrollConnection =
                        PagerDefaults.pageNestedScrollConnection(
                            state = pagerState,
                            orientation = Orientation.Horizontal,
                        ),
                    pageContent = { page ->
                        when (page) {
                            SELECTED_CHANNELS_PAGE -> {
                                SelectedChannelsPage(
                                    selectedChannels = selected,
                                    onAction = onAction,
                                )
                            }

                            else -> {
                                AvailableChannelsPage(
                                    selectedChannels = channelsList,
                                    onAction = onAction,
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@Composable
private fun ChannelSettingsContentPreview() {
    TvGuideTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                ChannelSettingsContent(
                    name = "My List",
                    viewState = ChannelSettingsState(),
                    selected = persistentListOf(
                        SelectionChannel(channelId = "1", channelName = "TV1000 Comedy"),
                        SelectionChannel(channelId = "2", channelName = "Discovery"),
                    ),
                    allChannels = persistentListOf(
                        SelectionChannel(channelId = "1", channelName = "TV1000 Comedy"),
                        SelectionChannel(channelId = "2", channelName = "Discovery"),
                        SelectionChannel(channelId = "3", channelName = "National Geographic"),
                    ),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                )
            }
        }
    }
}
