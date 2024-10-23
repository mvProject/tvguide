package com.mvproject.tvprogramguide.ui.components.views

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_300
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tvprogramguide.utils.AppConstants.ROTATION_STATE_DOWN
import com.mvproject.tvprogramguide.utils.AppConstants.ROTATION_STATE_UP
import com.mvproject.tvprogramguide.utils.convertTimeToReadableFormat
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun ProgramItem(
    modifier: Modifier = Modifier,
    program: Program,
    onProgramClick: () -> Unit = {},
) {
    var expandedState by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) ROTATION_STATE_UP else ROTATION_STATE_DOWN,
        label = "rotationState",
    )

    val cardAlpha =
        if (program.programProgress > PROGRESS_STATE_COMPLETE) {
            MaterialTheme.dimens.alpha50
        } else {
            MaterialTheme.dimens.alphaDefault
        }

    Column(
        modifier =
        modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .animateContentSize(
                animationSpec =
                tween(
                    durationMillis = ANIM_DURATION_300,
                    easing = LinearOutSlowInEasing,
                ),
            ),
    ) {
        ListItem(
            colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            leadingContent = {
                val isSelected =
                    program.programProgress == COUNT_ZERO_FLOAT &&
                            program.scheduledId != null

                TimeItem(
                    time = program.dateTimeStart.convertTimeToReadableFormat(),
                    modifier = Modifier.alpha(cardAlpha),
                    isSelected = isSelected,
                    onTimeClick = {
                        if (program.programProgress == COUNT_ZERO_FLOAT) {
                            onProgramClick()
                        }
                    },
                )
            },
            headlineContent = {
                Text(
                    text = program.title,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingContent = {
                if (program.description.isNotEmpty()) {
                    IconButton(
                        modifier =
                        Modifier
                            .alpha(cardAlpha)
                            .rotate(rotationState),
                        onClick = {
                            expandedState = !expandedState
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop-Down Arrow",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            },
        )

        if (program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE) {
            LinearProgressIndicator(
                progress = { program.programProgress },
                modifier = Modifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.inverseOnSurface,
                color = MaterialTheme.colorScheme.tertiary,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {}
            )
        }

        AnimatedContent(
            targetState = expandedState,
            label = "description",
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = slideInVertically { height -> height },
                    initialContentExit = fadeOut()
                )
            }
        ) { isVisible ->
            if (isVisible) {
                Text(
                    text = program.description,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.size2)
                        .padding(
                            horizontal = MaterialTheme.dimens.size8,
                            vertical = MaterialTheme.dimens.size4,
                        ),
                    color =
                    MaterialTheme.colorScheme.onSurface
                        .copy(alpha = MaterialTheme.dimens.alpha80),
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ProgramItemPreview() {
    TvGuideTheme {
        val current = Clock.System.now().toEpochMilliseconds()
        ProgramItem(
            program =
            Program(
                programId = "",
                dateTimeStart = current - 30.minutes.inWholeMilliseconds,
                dateTimeEnd = current + 30.minutes.inWholeMilliseconds,
                title = stringResource(id = R.string.app_name),
                description = stringResource(id = R.string.hint_search),
            ),
        )
    }
}