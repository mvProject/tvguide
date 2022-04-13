package com.mvproject.tvprogramguide.ui.usercustomlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.NoItemsScreen
import com.mvproject.tvprogramguide.components.dialogs.ShowDialog
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBackAndAction
import com.mvproject.tvprogramguide.data.model.CustomList
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.usercustomlist.components.UserCustomList
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
fun UserCustomListScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val userCustomListViewModel: UserCustomListViewModel = hiltViewModel()
    SideEffect {
        Timber.i("testing UserCustomListScreen SideEffect")
    }

    val lists = userCustomListViewModel.customs.collectAsState().value

    UserCustomListContent(
        customsLists = lists,
        onAction = userCustomListViewModel::processAction,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@ExperimentalMaterialApi
@Composable
private fun UserCustomListContent(
    customsLists: List<CustomList>,
    onAction: (action: UserListAction) -> Unit,
    onItemClick: (item: String) -> Unit,
    onBackClick: () -> Unit
) {
    SideEffect {
        Timber.i("testing UserCustomListContent SideEffect")
    }
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
