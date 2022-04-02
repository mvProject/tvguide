package com.mvproject.tvprogramguide.ui.usercustomlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@ExperimentalMaterialApi
@Composable
fun UserCustomListItem(
    modifier: Modifier = Modifier,
    listName: String,
    txtColor: Color  = MaterialTheme.colors.onPrimary,
    tintColor: Color  = MaterialTheme.colors.onPrimary,
    backgroundColor: Color = MaterialTheme.colors.primary,
    onItemAction: () -> Unit = {},
    onDeleteAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clickable {
                onItemAction()
            }
            .background(color = backgroundColor)
            .padding(MaterialTheme.dimens.size8)

    ) {
        Text(
            text = listName,
            fontSize = MaterialTheme.dimens.font14,
            style = MaterialTheme.appTypography.textMedium,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = MaterialTheme.dimens.size8),

            color = txtColor
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Icon(
            imageVector = Icons.Outlined.Delete,
            tint = tintColor,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    onDeleteAction()
                }
        )
    }
}

@ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun CustomListItemItemPreviewDark() {
    TvGuideTheme(true) {
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