package com.mvproject.tvprogramguide.ui.usercustomlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.NoItemsScreen
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowDialog
import com.mvproject.tvprogramguide.ui.components.toolbars.ToolbarWithBack
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.usercustomlist.components.UserCustomList
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel

@Composable
fun UserCustomListScreen(
    viewModel: UserCustomListViewModel,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.customs.collectAsState()

    UserCustomListContent(
        userLists = state,
        onAction = viewModel::processAction,
        onItemClick = onItemClick,
        onBackClick = onBackClick
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
                title = stringResource(id = R.string.custom_channels_list_title),
                onBackClick = onBackClick
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.secondary,
                onClick = {
                    isDialogOpen.value = true
                }) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size32)
                )
            }
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.primary)
                .padding(inner)
                .imePadding()

        ) {
            when {
                userLists.isEmpty() -> {
                    NoItemsScreen(
                        title = stringResource(id = R.string.user_lists_empty)
                    )
                }
                else -> {
                    UserCustomList(
                        list = userLists,
                        onItemClick = { item ->
                            onItemClick(item.listName)
                        },
                        onDeleteClick = { item ->
                            onAction(UserListAction.DeleteList(item))
                        }
                    )
                }
            }
        }
        ShowDialog(isDialogOpen) { name ->
            onAction(UserListAction.AddList(name))
        }
    }
}
