package com.mvproject.tvprogramguide.ui.screens.usercustomlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowAddNewDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.components.views.NoItemsScreen
import com.mvproject.tvprogramguide.ui.components.views.UserCustomListItem
import com.mvproject.tvprogramguide.ui.screens.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun UserCustomListScreen(
    viewModel: UserCustomListViewModel,
    onNavigateItem: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.customs.collectAsStateWithLifecycle()

    UserCustomListContent(
        userLists = state,
        onAction = viewModel::processAction,
        onItemClick = onNavigateItem,
        onBackClick = onNavigateBack
    )
}

@Composable
private fun UserCustomListContent(
    userLists: List<UserChannelsList>,
    onAction: (action: UserListAction) -> Unit,
    onItemClick: (item: String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToolbarWithBack(
                title = stringResource(id = R.string.title_custom_channels_list),
                onBackClick = onBackClick
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    isDialogOpen.value = true
                }
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size32)
                )
            }
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .imePadding()
        ) {
            when {
                userLists.isEmpty() -> {
                    NoItemsScreen(
                        title = stringResource(id = R.string.msg_user_lists_empty)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(userLists) { item ->
                            UserCustomListItem(
                                listName = item.listName,
                                onItemAction = { onItemClick(item.listName) },
                                onDeleteAction = { onAction(UserListAction.DeleteList(item)) }
                            )
                        }
                    }
                }
            }
        }
        ShowAddNewDialog(isDialogOpen) { name ->
            onAction(UserListAction.AddList(name))
        }
    }
}
