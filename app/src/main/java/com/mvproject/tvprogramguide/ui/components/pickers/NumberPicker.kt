package com.mvproject.tvprogramguide.ui.components.pickers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
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
    onValueChange: (Int) -> Unit = {}
) {
    val number = remember { mutableIntStateOf(default) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PickerButton(
            size = height,
            drawable = R.drawable.ic_minus,
            enabled = number.intValue > min,
            onClick = {
                if (number.intValue > min) number.intValue--
                onValueChange(number.intValue)
            }
        )

        Text(
            text = number.intValue.toString(),
            modifier = Modifier
                .padding(MaterialTheme.dimens.size8)
                .height(IntrinsicSize.Max),
            style = MaterialTheme.typography.headlineMedium,
        )

        PickerButton(
            size = height,
            drawable = R.drawable.ic_plus,
            enabled = number.intValue < max,
            onClick = {
                if (number.intValue < max) number.intValue++
                onValueChange(number.intValue)
            }
        )
    }
}

@Composable
fun PickerButton(
    size: Dp = MaterialTheme.dimens.size24,
    @DrawableRes drawable: Int = R.drawable.ic_minus,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val contentDesc = LocalContext.current.resources.getResourceName(drawable)

    val backgroundColor = if (enabled)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.tertiaryContainer

    val textColor = if (enabled)
        MaterialTheme.colorScheme.onTertiary
    else
        MaterialTheme.colorScheme.onTertiaryContainer

    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Icon(
            painterResource(id = drawable),
            modifier = Modifier.size(size),
            contentDescription = contentDesc,
        )
    }

    /*    Image(
            painter = painterResource(id = drawable),
            contentDescription = contentDesc,
            modifier = Modifier
                .padding(MaterialTheme.dimens.size8)
                .background(backgroundColor, CircleShape)
                .clip(CircleShape)
                .size(size = size)
                .clickable(
                    enabled = enabled,
                    onClick = { onClick() }
                ),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
        )*/
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
fun PreviewNumberPicker() {
    TvGuideTheme() {
        NumberPicker()
    }
}
