package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun TimeItem(time: String) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(MaterialTheme.dimens.size4)
    ) {
        Text(
            text = time,
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemPreview() {
    TvGuideTheme() {
        TimeItem(time = "12:05")
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemPreviewDark() {
    TvGuideTheme(true) {
        TimeItem(time = "12:05")
    }
}