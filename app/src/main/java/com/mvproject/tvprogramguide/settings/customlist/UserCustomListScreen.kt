package com.mvproject.tvprogramguide.settings.customlist

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.ToolbarWithBackAndAction

@ExperimentalMaterialApi
@Composable
fun UserCustomListScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val settingsChannelsListViewModel: SettingsChannelsListViewModel = hiltViewModel()

    val state = settingsChannelsListViewModel.customs.collectAsState()
    Column {
        ToolbarWithBackAndAction(
            title = stringResource(id = R.string.custom_channels_list_title),
            onBackClick = onBackClick,
            onActionClick = {
                // todo add new list from dialog
                //alertDialog?.cancel()
                //alertDialog = createAddDialog(
                //    activity = requireActivity(),
                //    title = getString(R.string.add_new_custom_list),
                //) { result ->
                //    settingsChannelsListViewModel.addCustomList(result)
                //    alertDialog?.cancel()
                //}.apply {
                //    show()
                //}
            }
        )

        UserCustomList(
            list = state.value,
            onItemClick = { item ->
                onItemClick(item.name)
            },
            onDeleteClick = { item ->
                settingsChannelsListViewModel.deleteList(item)
            }
        )
    }
}