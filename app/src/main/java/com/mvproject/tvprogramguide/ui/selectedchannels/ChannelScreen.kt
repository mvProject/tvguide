package com.mvproject.tvprogramguide.ui.selectedchannels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvproject.tvprogramguide.components.ToolbarWithOptions
import com.mvproject.tvprogramguide.model.data.Channel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ChannelScreen(
    onSettingsClick: () -> Unit,
    onChannelClick: (Channel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val programsViewModel: ProgramsViewModel = hiltViewModel()

        val selectedListState = programsViewModel.selectedList.collectAsState()
        val selectedPrograms = programsViewModel.selectedPrograms.collectAsState()

        programsViewModel.checkSavedList()
        programsViewModel.reloadChannels()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ToolbarWithOptions(
                title = selectedListState.value,
                onSelectClick = {
                    // todo add selection dialog
                    // alertDialog?.cancel()
                    // alertDialog = createSelectDialog(
                    //     activity = requireActivity(),
                    //     initialValue = programsViewModel.selectedList.value,
                    //     options = programsViewModel.availableLists
                    // ) { result ->
                    //     programsViewModel.saveSelectedList(result)
                    //     alertDialog?.cancel()
                    // }.apply {
                    //     show()
                    // }
                },
                onSettingsClick = onSettingsClick
            )

            ChannelList(selectedPrograms.value) {
                onChannelClick(it)
            }

        }


    }
}