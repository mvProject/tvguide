package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

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
        border = BorderStroke(
            width = MaterialTheme.dimens.size1,
            color = MaterialTheme.colorScheme.inverseOnSurface
        ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = listName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            FilledIconButton(
                onClick = onDeleteAction,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete"
                )
            }
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
