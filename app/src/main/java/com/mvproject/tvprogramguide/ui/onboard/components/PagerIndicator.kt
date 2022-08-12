package com.mvproject.tvprogramguide.ui.onboard.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun PagerIndicator(modifier: Modifier, size: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(size) {
            IndicateIcon(
                isSelected = it == currentPage
            )
        }
    }
}

@Composable
fun IndicateIcon(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) MaterialTheme.dimens.size40 else MaterialTheme.dimens.size20
    )

    Box(
        modifier = Modifier
            .padding(MaterialTheme.dimens.size2)
            .height(MaterialTheme.dimens.size10)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) {
                    MaterialTheme.colors.onBackground
                } else {
                    MaterialTheme.colors.onBackground
                        .copy(alpha = MaterialTheme.dimens.alpha50)
                }
            )
    )
}