package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.R

@Composable
fun DateItem(date: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()
        .background(color = colorResource(id = R.color.midnightblue))
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.DateRange,
            contentDescription = null,
            tint = colorResource(id = R.color.whitesmoke)
        )

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Text(
            text = date,
            modifier = Modifier
                .weight(1f),
            color = colorResource(id = R.color.whitesmoke)
        )
    }
}

@Preview
@Composable
fun DateItemView() {
    DateItem("22.03.2012")
}