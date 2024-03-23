package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.PROGRAM_TIME_MEASURE_COUNT
import com.mvproject.tvprogramguide.utils.AppConstants.PROGRAM_TIME_MEASURE_DELIMITER

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    time: String,
    isSelected: Boolean = false,
    onTimeClick: () -> Unit = {},
) {
    val datTime =
        time
            .split(PROGRAM_TIME_MEASURE_DELIMITER)
            .take(PROGRAM_TIME_MEASURE_COUNT)

    val timeColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "textColor",
    )

    val backColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.inverseOnSurface
        },
        label = "backColor",
    )

    Row(
        modifier =
            modifier
                .wrapContentSize()
                .clickable(
                    onClick = onTimeClick,
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .drawBehind {
                    drawRect(color = backColor)
                }
                .padding(
                    horizontal = MaterialTheme.dimens.size8,
                    vertical = MaterialTheme.dimens.size4,
                ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(MaterialTheme.dimens.size22),
            text = datTime.first(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = timeColor,
        )
        Text(
            text = PROGRAM_TIME_MEASURE_DELIMITER,
            style = MaterialTheme.typography.bodySmall,
            color = timeColor,
        )
        Text(
            modifier = Modifier.width(MaterialTheme.dimens.size22),
            text = datTime.last(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = timeColor,
        )
    }
}

@PreviewLightDark
@Composable
fun TimeItemPreview() {
    TvGuideTheme {
        Column {
            TimeItem(time = "06:55")
            TimeItem(time = "10:11", isSelected = true)
            TimeItem(time = "11:05")
            TimeItem(time = "17:21", isSelected = true)
            TimeItem(time = "20:37")
        }
    }
}
