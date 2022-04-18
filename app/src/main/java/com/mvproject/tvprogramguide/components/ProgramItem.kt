package com.mvproject.tvprogramguide.components

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.ANIM_DURATION_300
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.utils.AppConstants.OPACITY_60

@ExperimentalMaterialApi
@Composable
fun ProgramItem(
    prgTime: String,
    prgTitle: String,
    prgDescription: String,
    progressValue: Float = COUNT_ZERO_FLOAT
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = MaterialTheme.dimens.size48)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = ANIM_DURATION_300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(MaterialTheme.dimens.sizeZero),
        onClick = {
            if (prgDescription.isNotEmpty()) {
                expandedState = !expandedState
            }
        },
        elevation = MaterialTheme.dimens.size2
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = MaterialTheme.dimens.size42),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeItem(time = prgTime)

                Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size4))

                Text(
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight6),
                    text = prgTitle,
                    fontSize = MaterialTheme.dimens.font14,
                    style = MaterialTheme.appTypography.textMedium,
                    overflow = TextOverflow.Ellipsis
                )
                if (prgDescription.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .alpha(ContentAlpha.medium)
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

            if (progressValue > COUNT_ZERO_FLOAT) {
                LinearProgressIndicator(
                    progress = progressValue,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }

            if (expandedState) {
                Text(
                    text = prgDescription,
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

@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun ExpandableCardPreview() {
    TvGuideTheme() {
        Column() {
            ProgramItem(
                prgTime = "11:35",
                prgTitle = "My Title",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                    "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                progressValue = 0.1f
            )
            ProgramItem(
                prgTime = "10:01",
                prgTitle = "My Title",
                prgDescription = ""
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun ExpandableCardPreviewDark() {
    TvGuideTheme(true) {
        Column() {
            ProgramItem(
                prgTime = "11:35",
                prgTitle = "My Title",
                prgDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                    "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                progressValue = 0.1f
            )
            ProgramItem(
                prgTime = "10:01",
                prgTitle = "My Title",
                prgDescription = ""
            )
        }
    }
}
