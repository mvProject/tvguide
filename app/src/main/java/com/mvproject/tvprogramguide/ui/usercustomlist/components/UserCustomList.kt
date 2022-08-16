package com.mvproject.tvprogramguide.ui.usercustomlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
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
            .background(color = MaterialTheme.colors.background),
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
