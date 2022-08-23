package com.mvproject.tvprogramguide.ui.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ToolbarWithOptions(
    title: String,
    onSelectClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = MaterialTheme.colors.primary)
            .padding(MaterialTheme.dimens.size4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight1)
                .clickable {
                    onSelectClick()
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Drop-Down Arrow"
            )
            Spacer(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.size4)
            )
            Text(
                text = title,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.dimens.font18,
                style = MaterialTheme.appTypography.textBold
            )
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.size8)
        )

        Icon(
            Icons.Outlined.Settings,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(MaterialTheme.dimens.size32)
                .clickable {
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
