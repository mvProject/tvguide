package com.mvproject.tvprogramguide.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ToolbarWithOptions(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.default_toolbar_title),
    toolbarBackgroundColor: Color = MaterialTheme.colors.primary,
    titleColor: Color = MaterialTheme.colors.onPrimary,
    actionTintColor: Color = MaterialTheme.colors.onPrimary,
    onSelectClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clickable {
                onSelectClick()
            }
            .background(color = toolbarBackgroundColor)
            .padding(MaterialTheme.dimens.size4),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight1),
            color = titleColor,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.dimens.font14,
            style = MaterialTheme.appTypography.textSemiBold
        )

        Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8))

        Icon(
            Icons.Outlined.Settings,
            contentDescription = null,
            tint = actionTintColor,
            modifier = Modifier.clickable {
                onSettingsClick()
            }
        )
    }
}

@Preview
@Composable
fun ToolbarWithOptionsView() {
    TvGuideTheme() {
        ToolbarWithOptions(title = "TetstTitle")
    }
}

@Preview
@Composable
fun ToolbarWithOptionsDarkView() {
    TvGuideTheme(true) {
        ToolbarWithOptions(title = "TetstTitle")
    }
}
