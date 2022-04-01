package com.mvproject.tvprogramguide.ui.usercustomlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.tvprogramguide.data.entity.CustomListEntity
import com.mvproject.tvprogramguide.theme.appColors

@ExperimentalMaterialApi
@Composable
fun UserCustomList(
    modifier: Modifier = Modifier,
    list: List<CustomListEntity>,
    backgroundColor: Color = MaterialTheme.colors.primary,
    onItemClick: (CustomListEntity) -> Unit,
    onDeleteClick: (CustomListEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .background(color = backgroundColor),
    ) {
        items(list) { item ->
            UserCustomListItem(
                listName = item.name,
                tintColor = MaterialTheme.appColors.tintSecondary,
                onItemAction = { onItemClick(item) },
                onDeleteAction = { onDeleteClick(item) }
            )
        }
    }
}