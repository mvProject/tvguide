package com.mvproject.tvprogramguide.ui.usercustomlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.NoItemsScreen
import com.mvproject.tvprogramguide.components.dialogs.ShowDialog
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBackAndAction
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.usercustomlist.components.UserCustomList
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel

@Composable
fun UserCustomListScreen(
    viewModel: UserCustomListViewModel,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val lists = viewModel.customs.collectAsState().value

    UserCustomListContent(
        customsLists = lists,
        onAction = viewModel::processAction,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun UserCustomListContent(
    customsLists: List<UserChannelsList>,
    onAction: (action: UserListAction) -> Unit,
    onItemClick: (item: String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
    ) {
        ToolbarWithBackAndAction(
            title = stringResource(id = R.string.custom_channels_list_title),
            onBackClick = onBackClick,
            onActionClick = {
                isDialogOpen.value = true
            }
        )

        ShowDialog(isDialogOpen) { name ->
            onAction(UserListAction.AddList(name))
        }

        when {
            customsLists.isEmpty() -> {
                NoItemsScreen(
                    title = stringResource(id = R.string.user_lists_empty)
                )
            }
            else -> {
                UserCustomList(
                    list = customsLists,
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
}
