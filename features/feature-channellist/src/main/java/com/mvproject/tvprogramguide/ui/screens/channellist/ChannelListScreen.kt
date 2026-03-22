package com.mvproject.tvprogramguide.ui.screens.channellist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.feature.channellist.R
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowAddNewDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.ChannelListItem
import com.mvproject.tvprogramguide.ui.components.views.NoItemsScreen
import com.mvproject.tvprogramguide.ui.screens.channellist.action.ChannelListAction
import com.mvproject.tvprogramguide.ui.theme.dimens
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChannelListScreen(
    viewModel: ChannelListViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateItem: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    // ShowFeedback()

    val state by viewModel.customs.collectAsStateWithLifecycle()

    ChannelListScreen(
        userLists = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onAction = viewModel::processAction,
        onItemClick = onNavigateItem,
        onBackClick = onNavigateBack,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ChannelListScreen(
    userLists: ImmutableList<ChannelList>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (action: ChannelListAction) -> Unit,
    onItemClick: (item: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.title_custom_channels_list),
                onBackClick = onBackClick,
            )
        },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    isDialogOpen.value = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }
        },
    ) { inner ->
        Column(
            modifier =
                Modifier
                    .padding(inner)
                    .imePadding(),
        ) {
            when {
                userLists.isEmpty() -> {
                    NoItemsScreen(title = stringResource(id = R.string.msg_user_lists_empty))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement =
                            Arrangement.spacedBy(
                                MaterialTheme.dimens.size8,
                            ),
                        contentPadding =
                            PaddingValues(
                                vertical = MaterialTheme.dimens.size8,
                                horizontal = MaterialTheme.dimens.size4,
                            ),
                    ) {
                        items(userLists, key = { it.listName }) { item ->
                            ChannelListItem(
                                listName = item.listName,
                                isSelected = item.isSelected,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onItemAction = { onItemClick(item.listName) },
                                onItemSelect = { onAction(ChannelListAction.SelectList(item)) },
                                onDeleteAction = { onAction(ChannelListAction.DeleteList(item)) },
                            )
                        }
                    }
                }
            }
        }

        ShowAddNewDialog(isDialogOpen) { name ->
            onAction(ChannelListAction.AddList(name))
        }
    }
}

// todo review disabled

/*@Composable
private fun ShowFeedback() {
    val context = LocalContext.current as Activity
    val reviewManager = ReviewManagerFactory.create(context)
    reviewManager
        .requestReviewFlow()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewManager.launchReviewFlow(context, task.result)
            }
        }
}*/
