package com.mvproject.tvprogramguide.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.utils.AppConstants.ANIM_DURATION_300
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_DOWN
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_UP
import com.mvproject.tvprogramguide.data.utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

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
            val isSelected = program.programProgress == COUNT_ZERO_FLOAT
                    && program.scheduledId != null

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
                    fontSize = MaterialTheme.dimens.font14,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.appTypography.textMedium,
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
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Drop-Down Arrow"

                        )
                    }
                }
            }

            if (program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE) {
                LinearProgressIndicator(
                    progress = program.programProgress,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colors.secondary,
                    backgroundColor = MaterialTheme.colors.primary,
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
                    fontSize = MaterialTheme.dimens.font12,
                    color = MaterialTheme.colors.onPrimary
                        .copy(alpha = MaterialTheme.dimens.alpha80),
                    style = MaterialTheme.appTypography.textNormal,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    // }
}