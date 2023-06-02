package com.mvproject.tvprogramguide.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.utils.AppConstants.ANIM_DURATION_300
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_DOWN
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_UP
import com.mvproject.tvprogramguide.data.utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

@Composable
fun ProgramItem(
    program: Program,
    onProgramClick: () -> Unit = {}
) {
    var expandedState by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) ROTATION_STATE_UP else ROTATION_STATE_DOWN
    )

    val cardAlpha =
        if (program.programProgress > PROGRESS_STATE_COMPLETE)
            MaterialTheme.dimens.alpha50
        else MaterialTheme.dimens.alphaDefault

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = ANIM_DURATION_300,
                    easing = LinearOutSlowInEasing
                )
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = MaterialTheme.dimens.size42),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isSelected = program.programProgress == COUNT_ZERO_FLOAT &&
                    program.scheduledId != null

            TimeItem(
                time = program.dateTimeStart.convertTimeToReadableFormat(),
                modifier = Modifier.alpha(cardAlpha),
                isSelected = isSelected,
                onTimeClick = {
                    if (program.programProgress == COUNT_ZERO_FLOAT) {
                        onProgramClick()
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.size4)
            )

            Text(
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight6)
                    .alpha(cardAlpha),
                text = program.title,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
            if (program.description.isNotEmpty()) {
                IconButton(
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight1)
                        .alpha(cardAlpha)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        if (program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE) {
            LinearProgressIndicator(
                progress = program.programProgress,
                modifier = Modifier
                    .fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.inverseOnSurface,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        AnimatedVisibility(
            visible = expandedState,
            enter = slideInVertically {
                with(density) { -20.dp.roundToPx() }
            } + fadeIn(initialAlpha = MaterialTheme.dimens.alpha30),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Text(
                text = program.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size2)
                    .padding(
                        horizontal = MaterialTheme.dimens.size8,
                        vertical = MaterialTheme.dimens.size4
                    ),
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = MaterialTheme.dimens.alpha80),
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProgramItem() {
    TvGuideTheme() {
        val current = Clock.System.now().toEpochMilliseconds()
        ProgramItem(
            program = Program(
                dateTimeStart = current - 30.minutes.inWholeMilliseconds,
                dateTimeEnd = current + 30.minutes.inWholeMilliseconds,
                title = stringResource(id = R.string.app_name),
                description = stringResource(id = R.string.hint_search)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkProgramItem() {
    TvGuideTheme(darkTheme = true) {
        val current = Clock.System.now().toEpochMilliseconds()
        ProgramItem(
            program = Program(
                dateTimeStart = current - 30.minutes.inWholeMilliseconds,
                dateTimeEnd = current + 30.minutes.inWholeMilliseconds,
                title = stringResource(id = R.string.app_name),
                description = stringResource(id = R.string.hint_search)
            )
        )
    }
}
