package com.mvproject.tvprogramguide.ui.usercustomlist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.NoItemsScreen
import com.mvproject.tvprogramguide.ui.components.dialogs.ShowAddNewDialog
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
        ShowAddNewDialog(isDialogOpen) { name ->
            onAction(UserListAction.AddList(name))
        }
    }
}
