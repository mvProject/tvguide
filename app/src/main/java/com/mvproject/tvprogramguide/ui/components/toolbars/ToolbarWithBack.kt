package com.mvproject.tvprogramguide.ui.components.toolbars

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ToolbarWithBack(
    title: String = stringResource(id = R.string.default_toolbar_title),
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = MaterialTheme.colors.primary)
            .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Outlined.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(MaterialTheme.dimens.size32)
                .clickable {
                    onBackClick()
                }
        )

        Spacer(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.size8)
        )

        Text(
            text = title,
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight1),
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.dimens.font18,
            style = MaterialTheme.appTypography.textMedium
        )
    }
}

@Preview
@Composable
fun ToolbarWithBackView() {
    TvGuideTheme {
        ToolbarWithBack(title = "TetstTitle")
    }
}

@Preview
@Composable
fun ToolbarWithBackDarkView() {
    TvGuideTheme(true) {
        ToolbarWithBack(title = "TetstTitle")
    }
}
