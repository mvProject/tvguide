package com.mvproject.tvprogramguide.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ToolbarWithBackAndAction(
    title: String,
    toolbarBackgroundColor: Color = MaterialTheme.colors.primary,
    titleColor: Color = MaterialTheme.colors.onPrimary,
    backTintColor: Color = MaterialTheme.colors.onPrimary,
    actionTintColor: Color = MaterialTheme.colors.onPrimary,
    onBackClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = toolbarBackgroundColor)
            .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.ArrowBack,
            contentDescription = null,
            tint = backTintColor,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            color = titleColor,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.dimens.font14,
            style = MaterialTheme.appTypography.textMedium
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Icon(
            Icons.Outlined.Add,
            contentDescription = null,
            tint = actionTintColor,
            modifier = Modifier.clickable {
                onActionClick()
            }
        )
    }
}

@Preview
@Composable
fun ToolbarWithBackAndActionView() {
    TvGuideTheme() {
        ToolbarWithBackAndAction("TetstTitle")
    }
}

@Preview
@Composable
fun ToolbarWithBackAndActionDarkView() {
    TvGuideTheme(true) {
        ToolbarWithBackAndAction("TetstTitle")
    }
}