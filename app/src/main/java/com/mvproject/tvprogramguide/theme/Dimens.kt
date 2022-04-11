package com.mvproject.tvprogramguide.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.*

data class Dimens(
    val sizeZero: Dp = 0.dp,
    val size2: Dp = 2.dp,
    val size4: Dp = 4.dp,
    val size5: Dp = 5.dp,
    val size6: Dp = 6.dp,
    val size8: Dp = 8.dp,
    val size10: Dp = 10.dp,
    val size12: Dp = 12.dp,
    val size14: Dp = 14.dp,
    val size16: Dp = 16.dp,
    val size18: Dp = 18.dp,
    val size20: Dp = 20.dp,
    val size22: Dp = 22.dp,
    val size24: Dp = 24.dp,
    val size26: Dp = 26.dp,
    val size28: Dp = 28.dp,
    val size30: Dp = 30.dp,
    val size32: Dp = 32.dp,
    val size34: Dp = 34.dp,
    val size38: Dp = 38.dp,
    val size44: Dp = 44.dp,
    val size46: Dp = 46.dp,
    val size48: Dp = 48.dp,
    val size50: Dp = 50.dp,
    val size96: Dp = 96.dp,

    val font12: TextUnit = 12.sp,
    val font14: TextUnit = 14.sp,
    val font16: TextUnit = 16.sp,
    val font18: TextUnit = 18.sp,
    val font20: TextUnit = 20.sp,

    val weight1: Float = 1f,
    val weight6: Float = 6f
)

internal val LocalDimens = staticCompositionLocalOf { Dimens() }

val MaterialTheme.dimens: Dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current