package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BackHand
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.R

@Composable
fun ToolbarWithBack(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clickable {
                onBackClick()
            }
            .background(color = colorResource(id = R.color.midnightblue))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.ArrowBack,
            contentDescription = null,
            tint = colorResource(id = R.color.whitesmoke)
        )

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            color = colorResource(id = R.color.whitesmoke),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ToolbarWithBackView() {
    ToolbarWithBack("TetstTitle", onBackClick = {})
}