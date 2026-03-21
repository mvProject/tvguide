package com.mvproject.tvprogramguide.utils

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.geometry.Rect

// NavHost default transitions (Material 3 Emphasized motion)
val navEnterTransition =
    fadeIn(tween(300, easing = FastOutSlowInEasing)) +
            scaleIn(tween(300, easing = FastOutSlowInEasing), initialScale = 0.92f)

val navExitTransition =
    fadeOut(tween(200, easing = FastOutLinearInEasing))

val navPopEnterTransition =
    fadeIn(tween(300, easing = FastOutSlowInEasing))

val navPopExitTransition =
    fadeOut(tween(200, easing = FastOutLinearInEasing)) +
            scaleOut(tween(200, easing = FastOutLinearInEasing), targetScale = 0.92f)

// Content transitions inside sharedBounds (Container Transform)
// The BoundsTransform spring handles spatial movement; content just fades subtly
val sharedBoundsEnter = fadeIn(tween(150, easing = LinearEasing))
val sharedBoundsExit = fadeOut(tween(75, easing = LinearEasing))

// Spring BoundsTransform for Container Transform morphing (M3 Emphasized)
@OptIn(ExperimentalSharedTransitionApi::class)
val containerTransformBoundsTransform =
    BoundsTransform { _, _ ->
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = Rect.VisibilityThreshold,
        )
    }
