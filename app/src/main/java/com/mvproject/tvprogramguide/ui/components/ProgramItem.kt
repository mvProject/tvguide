package com.mvproject.tvprogramguide.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mvproject.tvprogramguide.data.utils.AppConstants.OPACITY_30
import com.mvproject.tvprogramguide.data.utils.AppConstants.OPACITY_50
import com.mvproject.tvprogramguide.data.utils.AppConstants.OPACITY_60
import com.mvproject.tvprogramguide.data.utils.AppConstants.OPACITY_DEFAULT
import com.mvproject.tvprogramguide.data.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_DOWN
import com.mvproject.tvprogramguide.data.utils.AppConstants.ROTATION_STATE_UP
import com.mvproject.tvprogramguide.data.utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.theme.appColors
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
        if (program.programProgress > PROGRESS_STATE_COMPLETE) OPACITY_50 else OPACITY_DEFAULT

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = ANIM_DURATION_300,
                    easing = LinearOutSlowInEasing
                )
            ),

        shape = RoundedCornerShape(MaterialTheme.dimens.sizeZero),
        elevation = MaterialTheme.dimens.size2
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = MaterialTheme.dimens.size42),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val timeColor =
                    if (program.programProgress == COUNT_ZERO_FLOAT && program.scheduledId != null)
                        MaterialTheme.appColors.textSelected
                    else
                        MaterialTheme.colors.onSurface

                TimeItem(
                    time = program.dateTimeStart.convertTimeToReadableFormat(),
                    modifier = Modifier.alpha(cardAlpha),
                    timeColor = timeColor,
                    onTimeClick = {
                        if (program.programProgress == COUNT_ZERO_FLOAT) {
                            onProgramClick()
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size4))

                Text(
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight6)
                        .alpha(cardAlpha),
                    text = program.title,
                    fontSize = MaterialTheme.dimens.font14,
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
                            contentDescription = "Drop-Down Arrow"
                        )
                    }
                }
            }

            if (program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE) {
                LinearProgressIndicator(
                    progress = program.programProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }

            AnimatedVisibility(
                visible = expandedState,
                enter = slideInVertically {
                    with(density) { -20.dp.roundToPx() }
                } + fadeIn(initialAlpha = OPACITY_30),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = program.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.size2)
                        .alpha(OPACITY_60)
                        .padding(
                            horizontal = MaterialTheme.dimens.size10,
                            vertical = MaterialTheme.dimens.size4
                        ),
                    fontSize = MaterialTheme.dimens.font12,
                    style = MaterialTheme.appTypography.textNormal,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
/*
@Composable
@Preview(showBackground = true)
fun ExpandableCardPreview() {
    TvGuideTheme() {
        Column() {
            ProgramItem(
                prgTime = "11:35",
                prgTitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                        "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                        "ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                progressValue = 0.1f
            )
            ProgramItem(
                prgTime = "10:01",
                prgTitle = "My Title",
                prgDescription = "",
                progressValue = 0.1f
            )

            ProgramItem(
                prgTime = "11:15",
                prgTitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                        "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                        "ullamco laboris nisi ut aliquip ex ea commodo consequat.",

                )
            ProgramItem(
                prgTime = "13:46",
                prgTitle = "My Title",
                prgDescription = ""
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExpandableCardPreviewDark() {
    TvGuideTheme(true) {
        Column() {
            ProgramItem(
                prgTime = "11:35",
                prgTitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                        "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                        "ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                progressValue = 0.1f
            )
            ProgramItem(
                prgTime = "10:01",
                prgTitle = "My Title",
                prgDescription = "",
                progressValue = 0.1f
            )

            ProgramItem(
                prgTime = "11:15",
                prgTitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                        "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                        "ullamco laboris nisi ut aliquip ex ea commodo consequat.",

                )
            ProgramItem(
                prgTime = "13:46",
                prgTitle = "My Title",
                prgDescription = ""
            )
        }
    }
}

 */
