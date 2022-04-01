package com.mvproject.tvprogramguide.ui.usercustomlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.NoItemsScreen
import com.mvproject.tvprogramguide.components.dialogs.ShowDialog
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBackAndAction
import com.mvproject.tvprogramguide.ui.usercustomlist.action.UserListAction
import com.mvproject.tvprogramguide.ui.usercustomlist.components.UserCustomList
import com.mvproject.tvprogramguide.ui.usercustomlist.viewmodel.UserCustomListViewModel

@ExperimentalMaterialApi
@Composable
fun UserCustomListScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val userCustomListViewModel: UserCustomListViewModel = hiltViewModel()

    val isDialogOpen = remember { mutableStateOf(false) }

    val movies = userCustomListViewModel.customs.collectAsState().value

    Column(
        modifier = modifier
            .background(color = backgroundColor)
    ) {
        ToolbarWithBackAndAction(
            title = stringResource(id = R.string.custom_channels_list_title),
            onBackClick = onBackClick,
            onActionClick = {
                isDialogOpen.value = true
            }
        )

        ShowDialog(isDialogOpen) { name ->
            userCustomListViewModel.processAction(UserListAction.AddList(name))
        }

        when {
            movies.isEmpty() -> {
                NoItemsScreen()
            }
            else -> {
                UserCustomList(
                    list = movies,
                    onItemClick = { item ->
                        onItemClick(item.name)
                    },
                    onDeleteClick = { item ->
                        userCustomListViewModel.processAction(UserListAction.DeleteList(item))
                    }
                )
            }
        }
    }
}