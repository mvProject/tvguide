package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimeItem(time: String) {
    Row(modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight()
        .padding(4.dp)
    ){
        Text(text = time)
    }
}

@Preview
@Composable
fun TimeItemPreview() {
    TimeItem(time = "12:05")
}