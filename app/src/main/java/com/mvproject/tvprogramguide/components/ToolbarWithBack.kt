package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ToolbarWithBack(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clickable {
                onBackClick()
            }
            .background(color = MaterialTheme.appColors.backgroundPrimary)
            .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.appColors.textPrimary
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            color = MaterialTheme.appColors.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ToolbarWithBackView() {
    ToolbarWithBack("TetstTitle", onBackClick = {})
}