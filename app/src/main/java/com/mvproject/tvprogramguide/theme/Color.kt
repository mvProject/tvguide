package com.mvproject.tvprogramguide.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
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

private val DarkColorPalette = darkColors(
    primary = lightBlack,
    background = lightBlackAlpha50,
    onPrimary = chocolate,
    onBackground = steelBlue
)

private val LightColorPalette = lightColors(
    primary = midnightBlue,
    background = darkSlateGray,
    onPrimary = lightSlateGray,
    onBackground = steelBlue
)

data class AppColors(
    val backgroundPrimary: Color,
    val textPrimary: Color,
    val backgroundSecondary: Color,
    val textSecondary: Color,
    val border: Color,
    val tintPrimary: Color,
    val tintSecondary: Color,
    val material: Colors,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}

val LightColor = AppColors(
    backgroundPrimary = midnightBlue,
    textPrimary = lightSlateGray,
    backgroundSecondary = darkSlateGray,
    textSecondary = steelBlue,
    border = lightSteelBlueAlpha60,
    tintPrimary = steelBlue,
    tintSecondary = chocolate,
    material = LightColorPalette
)

val DarkColor = AppColors(
    backgroundPrimary = lightBlack,
    textPrimary = chocolate,
    backgroundSecondary = lightBlackAlpha50,
    textSecondary = steelBlue,
    border = chocolate,
    tintPrimary = steelBlue,
    tintSecondary = chocolate,
    material = DarkColorPalette
)
internal val LocalColors = staticCompositionLocalOf { LightColor }
val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
