package com.mvproject.tvprogramguide.ui.usercustomlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun UserCustomListItem(
    listName: String,
    onItemAction: () -> Unit = {},
    onDeleteAction: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                onItemAction()
            },
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = listName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically)
                    .padding(start = MaterialTheme.dimens.size8)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Icon(
                imageVector = Icons.Outlined.Delete,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(MaterialTheme.dimens.size24)
                    .clickable {
                        onDeleteAction()
                    }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomListItemItemPreview() {
    TvGuideTheme {
        Column {
            UserCustomListItem(
                listName = "Channel1"
            )
            UserCustomListItem(
                listName = "Channel2",
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomListItemItemPreviewDark() {
    TvGuideTheme(darkTheme = true) {
        Column {
            UserCustomListItem(
                listName = "Channel1"
            )
            UserCustomListItem(
                listName = "Channel2",
            )
        }
    }
}
