package com.mvproject.tvprogramguide.ui.screens.settings.channels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.SelectionChannel
import com.mvproject.tvprogramguide.ui.components.search.SearchView
import com.mvproject.tvprogramguide.ui.components.views.ChannelSelectableItem
import com.mvproject.tvprogramguide.ui.screens.settings.channels.action.ChannelsAction
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailableChannelsPage(
    selectedChannels: List<SelectionChannel>,
    onAction: (action: ChannelsAction) -> Unit,
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            SearchView(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.size8),
            ) { selectedQuery ->
                onAction(ChannelsAction.ChannelFilter(query = selectedQuery))
            }
        },
    ) { padding ->

        LazyColumn(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .imePadding()
                    .imeNestedScroll(),
            state = listState,
            contentPadding =
                PaddingValues(
                    vertical = MaterialTheme.dimens.size8,
                    horizontal = MaterialTheme.dimens.size4,
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
        ) {
            items(selectedChannels) { chn ->
                ChannelSelectableItem(
                    channelLogo = chn.channelIcon,
                    channelName = chn.channelName,
                    isSelected = chn.isSelected,
                ) {
                    onAction(ChannelsAction.ToggleSelection(channel = chn))
                }
            }
        }
    }
}
