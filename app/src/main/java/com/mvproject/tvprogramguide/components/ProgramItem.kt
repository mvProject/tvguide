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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalMaterialApi
@Composable
fun ProgramItem(
    prgTime: String,
    prgTitle: String,
    prgDescription: String,
    progressValue: Float = 0f,
    padding: Dp = 4.dp
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(4.dp),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeItem(time = prgTime)
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = prgTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (prgDescription.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .alpha(ContentAlpha.medium)
                            .rotate(rotationState),
                        onClick = {
                            expandedState = !expandedState
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop-Down Arrow"
                        )
                    }
                }
            }
            if (progressValue > 0) {
                LinearProgressIndicator(progress = progressValue, modifier = Modifier.fillMaxWidth())
            }
            if (expandedState) {
                Text(
                    text = prgDescription,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    //  maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun ExpandableCardPreview() {
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