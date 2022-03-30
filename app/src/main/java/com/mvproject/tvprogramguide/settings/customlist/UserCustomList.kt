package com.mvproject.tvprogramguide.settings.customlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.components.UserCustomListItem
import com.mvproject.tvprogramguide.database.entity.CustomListEntity

@ExperimentalMaterialApi
@Composable
fun UserCustomList(
    list: List<CustomListEntity>,
    onItemClick: (CustomListEntity) -> Unit,
    onDeleteClick: (CustomListEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary),
    ) {
        items(list) { item ->
            UserCustomListItem(
                listName = item.name,
                onItemAction = { onItemClick(item) },
                onDeleteAction = { onDeleteClick(item) }
            )
        }
    }
}