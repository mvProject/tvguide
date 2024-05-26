package com.mvproject.tvprogramguide.ui.components.pickers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    height: Dp = MaterialTheme.dimens.size24,
    min: Int = 0,
    max: Int = 10,
    default: Int = min,
    onValueChange: (Int) -> Unit = {},
) {
    val number = remember { mutableIntStateOf(default) }

    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PickerButton(
            size = height,
            drawable = R.drawable.ic_minus,
            enabled = number.intValue > min,
            onClick = {
                if (number.intValue > min) number.intValue--
                onValueChange(number.intValue)
            },
        )

        Text(
            text = number.intValue.toString(),
            modifier =
                Modifier
                    .padding(MaterialTheme.dimens.size8),
            fontSize = MaterialTheme.dimens.font18,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
        )

        PickerButton(
            size = height,
            drawable = R.drawable.ic_plus,
            enabled = number.intValue < max,
            onClick = {
                if (number.intValue < max) number.intValue++
                onValueChange(number.intValue)
            },
        )
    }
}

@Composable
fun PickerButton(
    size: Dp = MaterialTheme.dimens.size24,
    @DrawableRes drawable: Int = R.drawable.ic_minus,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val contentDesc = LocalContext.current.resources.getResourceName(drawable)

    val textColor =
        if (enabled) {
            MaterialTheme.colorScheme.onTertiary
        } else {
            MaterialTheme.colorScheme.onTertiaryContainer
        }

    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        colors =
            IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = textColor,
            ),
    ) {
        Icon(
            painterResource(id = drawable),
            modifier = Modifier.size(size),
            contentDescription = contentDesc,
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewNumberPicker() {
    TvGuideTheme {
        NumberPicker()
    }
}

@PreviewLightDark
@Composable
fun PreviewPickerButton() {
    TvGuideTheme {
        Column {
            PickerButton()
            PickerButton(enabled = false)
        }
    }
}
