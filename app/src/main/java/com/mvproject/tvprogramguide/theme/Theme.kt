package com.mvproject.tvprogramguide.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun TvGuideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = if (darkTheme) {
        DarkColor
    } else {
        LightColor
    }

    CompositionLocalProvider(
        LocalColors provides appColors,
        LocalDimens provides Dimens(),
        LocalTypography provides AppTypography(),
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}