package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun TimeItem(time: String, modifier: Modifier = Modifier) {
    val datTime = time.split(":")
    Row(
        modifier = modifier
            .width(MaterialTheme.dimens.size44)
            .wrapContentHeight()
            .padding(
                horizontal = MaterialTheme.dimens.size4,
                vertical = MaterialTheme.dimens.size2
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = datTime.first(),
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold
        )
        Text(
            text = ":",
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold
        )
        Text(
            text = datTime.last(),
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemPreview() {
    TvGuideTheme() {
        Column {
            TimeItem(time = "06:55")
            TimeItem(time = "10:11")
            TimeItem(time = "11:05")
            TimeItem(time = "17:21")
            TimeItem(time = "20:37")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemPreviewDark() {
    TvGuideTheme(true) {
        Column {
            TimeItem(time = "06:55")
            TimeItem(time = "10:11")
            TimeItem(time = "11:05")
            TimeItem(time = "17:21")
            TimeItem(time = "20:37")
        }
    }
}
