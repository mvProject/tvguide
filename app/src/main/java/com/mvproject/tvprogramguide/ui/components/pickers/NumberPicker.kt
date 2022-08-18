package com.mvproject.tvprogramguide.ui.components.pickers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun NumberPicker(
    modifier: Modifier,
    height: Dp = MaterialTheme.dimens.size24,
    min: Int = 0,
    max: Int = 10,
    default: Int = min,
    onValueChange: (Int) -> Unit = {}
) {
    val number = remember { mutableStateOf(default) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PickerButton(
            size = height,
            drawable = R.drawable.ic_minus,
            enabled = number.value > min,
            onClick = {
                if (number.value > min) number.value--
                onValueChange(number.value)
            }
        )

        Text(
            text = number.value.toString(),
            modifier = Modifier
                .padding(MaterialTheme.dimens.size8)
                .height(IntrinsicSize.Max)
                .align(CenterVertically),
            fontSize = MaterialTheme.dimens.font14,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.appTypography.textBold,
        )

        PickerButton(
            size = height,
            drawable = R.drawable.ic_plus,
            enabled = number.value < max,
            onClick = {
                if (number.value < max) number.value++
                onValueChange(number.value)
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
        MaterialTheme.colors.secondary.copy(alpha = MaterialTheme.dimens.alpha50)
    else
        MaterialTheme.colors.secondary.copy(alpha = MaterialTheme.dimens.alpha30)

    Image(
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
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
    )
}
