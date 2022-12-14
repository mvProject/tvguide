package com.mvproject.tvprogramguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRAM_TIME_MEASURE_COUNT
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRAM_TIME_MEASURE_DELIMITER
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    time: String,
    isSelected: Boolean = false,
    onTimeClick: () -> Unit = {}
) {
    val datTime = time
        .split(PROGRAM_TIME_MEASURE_DELIMITER)
        .take(PROGRAM_TIME_MEASURE_COUNT)

    val timeColor =
        if (isSelected)
            MaterialTheme.colors.onSecondary
        else
            MaterialTheme.colors.onPrimary

    val backColor =
        if (isSelected)
            MaterialTheme.colors.secondary
        else
            MaterialTheme.colors.primary

    Row(
        modifier = modifier
            .width(MaterialTheme.dimens.size44)
            .wrapContentHeight()
            .clip(RoundedCornerShape(MaterialTheme.dimens.size8))
            .background(backColor)
            .clickable {
                onTimeClick()
            }
            .padding(
                horizontal = MaterialTheme.dimens.size4,
                vertical = MaterialTheme.dimens.size2
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = datTime.first(),
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold,
            color = timeColor
        )
        Text(
            text = PROGRAM_TIME_MEASURE_DELIMITER,
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold,
            color = timeColor
        )
        Text(
            text = datTime.last(),
            fontSize = MaterialTheme.dimens.font12,
            style = MaterialTheme.appTypography.textSemiBold,
            color = timeColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemPreview() {
    TvGuideTheme() {
        Column {
            TimeItem(time = "06:55")
            TimeItem(time = "10:11", isSelected = true)
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
            TimeItem(time = "11:05", isSelected = true)
            TimeItem(time = "17:21")
            TimeItem(time = "20:37")
        }
    }
}
