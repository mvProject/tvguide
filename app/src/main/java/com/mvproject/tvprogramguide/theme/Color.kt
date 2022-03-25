package com.mvproject.tvprogramguide.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val darkSlateGray = Color(0xFF24485e)
val steelBlue = Color(0xFF4a8fc4)
val lightBlack = Color(0xFF1C1D1D)
val lightBlackAlpha80 = Color(0x801C1D1D)
val lightBlackAlpha50 = Color(0x501C1D1D)
val chocolate = Color(0xFFEB4F27)
val lightSlateGray = Color(0xFF748794)
val lightSteelBlue = Color(0xFFb7c8d5)
val lightSteelBlueAlpha60 = Color(0x99b7c8d5)
val midnightBlue = Color(0xFF032d4b)

data class AppColors(
    val backgroundPrimary: Color,
    val textPrimary: Color,
    val backgroundSecondary: Color,
    val textSecondary: Color,
    val border: Color,
    val tintPrimary: Color,
    val tintSecondary: Color,
)

val LightColor = AppColors(
    backgroundPrimary = midnightBlue,
    textPrimary = lightSlateGray,
    backgroundSecondary = darkSlateGray,
    textSecondary = steelBlue,
    border = lightSteelBlueAlpha60,
    tintPrimary = steelBlue,
    tintSecondary = chocolate
)

val DarkColor = AppColors(
    backgroundPrimary = lightBlack,
    textPrimary = chocolate,
    backgroundSecondary = lightBlackAlpha50,
    textSecondary = steelBlue,
    border = chocolate,
    tintPrimary = steelBlue,
    tintSecondary = chocolate
)
internal val LocalColors = staticCompositionLocalOf { LightColor }
val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

