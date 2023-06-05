package com.mvproject.tvprogramguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRAM_TIME_MEASURE_COUNT
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRAM_TIME_MEASURE_DELIMITER
import com.mvproject.tvprogramguide.theme.TvGuideTheme
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
            MaterialTheme.colorScheme.onTertiaryContainer
        else
            MaterialTheme.colorScheme.onSurface

    val backColor =
        if (isSelected)
            MaterialTheme.colorScheme.tertiaryContainer
        else
            MaterialTheme.colorScheme.inverseOnSurface

    Row(
        modifier = modifier
            .width(MaterialTheme.dimens.size48)
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.extraSmall)
            .background(backColor)
            .clickable {
                onTimeClick()
            }
            .padding(
                horizontal = MaterialTheme.dimens.size4,
                vertical = MaterialTheme.dimens.size2
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = datTime.first(),
            style = MaterialTheme.typography.bodySmall,
            color = timeColor
        )
        Text(
            text = PROGRAM_TIME_MEASURE_DELIMITER,
            style = MaterialTheme.typography.labelSmall,
            color = timeColor
        )
        Text(
            text = datTime.last(),
            style = MaterialTheme.typography.bodySmall,
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
    TvGuideTheme(darkTheme = true) {
        Column {
            TimeItem(time = "06:55")
            TimeItem(time = "10:11")
            TimeItem(time = "11:05", isSelected = true)
            TimeItem(time = "17:21")
            TimeItem(time = "20:37")
        }
    }
}
