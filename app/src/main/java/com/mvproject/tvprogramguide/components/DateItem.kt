package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun DateItem(date: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()
        .background(color = MaterialTheme.colors.primary)
        .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Text(
            text = date,
            modifier = Modifier
                .weight(1f),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview
@Composable
fun DateItemView() {
    DateItem("22.03.2012")
}