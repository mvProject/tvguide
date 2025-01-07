package com.mvproject.tvprogramguide.utils

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.geometry.Rect
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_750

val openControlAnimation = fadeIn(animationSpec = tween(ANIM_DURATION_750))
val closeControlAnimation = fadeOut(animationSpec = tween(ANIM_DURATION_750))

val openScreenAnimation = fadeIn(tween(ANIM_DURATION_750)) +
        slideInVertically(
            animationSpec = tween(ANIM_DURATION_750),
            initialOffsetY = { it },
        )

val closeScreenAnimation = fadeOut(tween(ANIM_DURATION_750)) +
        slideOutVertically(
            animationSpec = tween(ANIM_DURATION_750),
            targetOffsetY = { it },
        )


@OptIn(ExperimentalSharedTransitionApi::class)
val slideOutDetailsBoundsTransform =
    BoundsTransform { initialBounds, targetBounds ->
        if (targetBounds.size.width < initialBounds.size.width) {
            keyframes {
                durationMillis = ANIM_DURATION_750
                Rect(
                    top = targetBounds.top,
                    bottom = targetBounds.bottom,
                    left = initialBounds.left,
                    right = initialBounds.right,
                ) atFraction 1.0f
            }
        } else {
            spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = Rect.VisibilityThreshold,
            )
        }
    }