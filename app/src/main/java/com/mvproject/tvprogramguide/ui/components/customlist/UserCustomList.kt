package com.mvproject.tvprogramguide.ui.components.customlist

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList

@Composable
fun UserCustomList(
    modifier: Modifier = Modifier,
    list: List<UserChannelsList>,
    onItemClick: (UserChannelsList) -> Unit,
    onDeleteClick: (UserChannelsList) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        items(list) { item ->
            UserCustomListItem(
                listName = item.listName,
                onItemAction = { onItemClick(item) },
                onDeleteAction = { onDeleteClick(item) }
            )
        }
    }
}
